package controllers

import java.sql.Timestamp
import java.util.{Calendar, Date}
import javax.inject.Inject
import javax.management.relation.RelationServiceNotRegisteredException

import play.api.cache.Cache
import play.api.Play.current

import scala.util.Try
import scala.concurrent.{Await, Future}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.{Cursor, QueryOpts}
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._
import play.api.i18n.{I18nSupport,MessagesApi}
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration.Duration


class MongoDBController @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with I18nSupport {


  //Read from table movies
  def moviecollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("testMovie"))


  def showings: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("showings"))

  def receipts: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("receipts"))


  def getreceipt(reservationId: String): Reservation = {
    val cursor: Future[Cursor[Reservation]] = receipts.map {
      _.find(Json.obj("reservationId" -> reservationId))
        .sort(Json.obj("created" -> -1))
        .cursor[Reservation]
    }
    val futureResList: Future[List[Reservation]] = cursor.flatMap(_.collect[List]())
    Await.result(futureResList, Duration.Inf).head
  }

  def getshowing(showingId: Int): Showing = {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj("showingId" -> showingId))
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val futureResList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    Await.result(futureResList, Duration.Inf).head
  }

  def updateShowingPage(): Action[AnyContent] = Action.async {
    implicit request =>
      val showingId = request.session.get("id").get
      val receiptId = request.session.get("reservationId").get
      val receipt = getreceipt(receiptId)
      val seats = receipt.seats
      val seatsNo = request.session.get("seatsNo").get.toInt
      val showing = getshowing(showingId.toInt)
      for (i <- seats) {
        showing.seats(i.head.toInt)(i.last.toInt) = 1
      }
      showing.seatsAvailable -= seatsNo
      receipt.paid=true
      val selector = BSONDocument("showingId" -> showingId.toInt) // looking for the record based on some field
      val selector1 = BSONDocument("reservationId" -> receiptId)
      val futureResult = showings.map(_.findAndUpdate(selector, showing))
      futureResult.map(_ => Ok(views.html.successPage(receipt)))
      val futureResultTwo = receipts.map(_.findAndUpdate(selector1, receipt))
      futureResultTwo.map(_ => Ok(views.html.successPage(receipt)))
  }


  def dateParse(date: String): Date = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    format.parse(date)
  }

  def isFuture(value: Date): Boolean = value.after(new Date)

  def isCurrent(value: Date): Boolean = value.before(new Date)


  //list movies for index
  def listIndexMovies: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      val upcomingMovies = for (i <- movies if (isFuture(dateParse(i.release_date)))) yield i
      val showingMovies = for (i <- movies if !(isFuture(dateParse(i.release_date)))) yield i
      Ok(views.html.index(movies)(upcomingMovies.drop(0))(showingMovies.drop(4))) //Drops movies to make overall count = 4

    }
  }

  //display Movies from database
  def listMovies: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      Ok(views.html.listings(movies))
    }
  }


  //Filter movies by genre
  def filterMovies(genre: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    var futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>

      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      val newmovies = for (i <- movies if (i.genres.contains(genre))) yield i
      Ok(views.html.listings(newmovies))
    }
  }

  //Upcoming movies
  def upcomingMovies: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    var futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      val newmovies = for (i <- movies if (isFuture(dateParse(i.release_date)))) yield i
      Ok(views.html.listings(newmovies))
    }
  }

  def replaceAgeRating(age: String): String = "images/" + age.toLowerCase + ".png"


  def movieInfo(id: Int): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj("movie_id" -> id)) // searching by a particular field
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureMovieList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureMovieList.map { movie =>
      Try(Ok(views.html.movieInfoPage(movie.head))).getOrElse(Ok(views.html.noSuchMovie(id)))
    }

  }

  def seeAddMovie = Action {
    Ok(views.html.addMovie(Movie.createMovie))
  }

  def getMovies(): List[Movie] = {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureMovieList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    Await.result(futureMovieList, Duration.Inf)
  }


  //adds Movies to database
  def addMovie() = Action {
    implicit request =>
      request.session.get("admin").map { user =>

        val formValidationResult = Movie.createMovie.bindFromRequest
        formValidationResult.fold({
          errors =>
            println(formValidationResult)

            BadRequest(views.html.addMovie(errors))

        }, { movie =>
          val futureResult = moviecollection.flatMap(_.insert(movie))

          futureResult.map(_ => Ok("Added user " + movie.title + " " + movie.genres))
          Redirect(routes.MongoDBController.listMovies())
        })

      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }
  }


  //Display all showings available to book
  def showingsView: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val showingsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    showingsList.map { showing =>

      Ok(views.html.showings(showing))
    }
  }


  def ticketSelection(id: Int) = Action {

    Ok(views.html.ticketselection(id)).withSession("id" -> s"$id")
  }


  //display grid for selecting seats
  def seating(total: Int, adult: Int, child: Int, concession: Int, seatsNo: Int): Action[AnyContent] = Action.async { implicit request =>
    val sId = request.session.get("id").get
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj("showingId" -> sId.toInt))
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    var seatsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    seatsList.map { showing =>
      val singleS = showing.head
      Ok(views.html.seating(singleS, seatsNo)).withSession(request.session + ("total" -> total.toString) + ("adult" -> adult.toString) + ("child" -> child.toString) +
        ("concession" -> concession.toString) + ("seatsNo" -> seatsNo.toString) + ("moviename" -> singleS.movieId) + ("date" -> singleS.date) + ("time" -> singleS.time) +
        ("room" -> singleS.roomId.toString))
    }
  }

  //get user details to complete booking
  def userInfo(seats: String): Action[AnyContent] = Action.async { implicit request =>
    val sId = request.session.get("id").get
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj("showingId" -> sId.toInt))
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val seatsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    seatsList.map { showing =>

      Ok(views.html.userinformation(showing.head)).withSession(request.session + ("seats" -> seats))

    }
  }

  def updateMoviePage(id: Int): Action[AnyContent] = Action.async {
    implicit request =>
      val cursor: Future[Cursor[Movie]] = moviecollection.map {
        _.find(Json.obj())
          .sort(Json.obj("created" -> 1))
          .cursor[Movie]
      }
      val futureMovieList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
      futureMovieList.map(
        movie =>
          Ok(views.html.editMovie(movie, Movie.createMovie.fill(movie(id)), id))
      )

  }

  def updateMovie(id: Int) = Action {

    implicit request =>
      request.session.get("admin").map { user =>

        val formValidationResult = Movie.createMovie.bindFromRequest
        formValidationResult.fold({ errors =>
          BadRequest(views.html.listings(getMovies()))
        }, { movies =>
          val movieList = getMovies()
          val selector = movieList(id)
          val futureResult = moviecollection.map(_.findAndUpdate(selector, movies))
          futureResult.map(_ => Ok("Added user " + movies.title))
          Redirect(routes.MongoDBController.listMovies())

        })
      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }


  }

  def updatePage(): Action[AnyContent] = Action {
    implicit request =>
      request.session.get("admin").map { user =>

        val movies = getMovies()
        Ok(views.html.showEdits(movies))

      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }
  }

  def deletePage(id: Int): Action[AnyContent] = Action {
    implicit request =>
      request.session.get("admin").map { user =>

        val movieList = getMovies()
        Ok(views.html.deleteMoviePage(movieList, id))

      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }
  }


  def deleteMovie(id: Int) = Action {
    implicit request =>
      request.session.get("admin").map { user =>

        val movieList = getMovies()
        val selector = movieList(id)
        val futureResult = moviecollection.map(_.findAndRemove(selector))
        Redirect(routes.MongoDBController.updatePage())

      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }
  }

  def adminPage() = Action {
    implicit request =>
      request.session.get("admin").map { user =>

        Ok(views.html.AdminControllerPage(user))

      }.getOrElse {
        Unauthorized(views.html.messagePage("You are not logged in!"))
      }
  }


  //before payment
  def payment(name: String, email: String): Action[AnyContent] = Action.async { implicit request =>
    val total = request.session.get("total").get.toInt
    val adult = request.session.get("adult").get.toInt
    val child = request.session.get("child").get.toInt
    val concession = request.session.get("concession").get.toInt
    val seats = request.session.get("seats").get
    val newseats = seats.split(',').toList.map(elem => elem.split(' ').toList).tail
    val moviename = request.session.get("moviename").get
    val date = request.session.get("date").get
    val time = request.session.get("time").get
    val room = request.session.get("room").get
    val currentTimestamp = new Timestamp(Calendar.getInstance.getTime.getTime).toString

    val reservation = new Reservation(currentTimestamp, name, email, adult, child, concession, newseats, total, moviename, date, time, room, false)
    val futureResult = receipts.flatMap(_.insert(reservation))
    futureResult.map(_ =>
      //the price in there that you want the checkout button to have
      Ok(views.html.payment(total.toString, reservation)).withSession(request.session + ("name" -> name) + ("email" -> email) + ("reservationId" -> currentTimestamp))
    )
  }

  //    def success() : Action[AnyContent]{
  //      Ok(views.html.successPage())
  //    }


}