package controllers

import java.util.Date
import javax.inject.Inject

import scala.concurrent.{Await, Future}
import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.data._
import play.api.data.Forms._


class MongoDBController @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with I18nSupport {


  //Read from table movies
  def moviecollection: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("movies"))

  def showings:Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("showings"))


  //display Movies from database
  def listMovies: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    var futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      Ok(views.html.listings(movies))
    }
  }

  //Filter movies by genre
  def filterMovies(genre: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj("genres" -> genre))
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    var futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      movies.map(m => m.age_rating = replaceAgeRating(m.age_rating))
      Ok(views.html.listings(movies))
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
      val newmovies = for(i<-movies if(isFuture(dateParse(i.release_date)))) yield i
      Ok(views.html.listings(newmovies))
    }
  }

  def replaceAgeRating(age: String): String = {
    age match {
      case "U" => "images/u.png"
      case "PG" => "images/pg.png"
      case "12" => "images/12.png"
      case "12A" => "/images/12a.png"
      case "15" => "images/15.png"
      case _ => "images/18.png"
    }
  }

  def dateParse(date: String):Date= {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    format.parse(date)
  }

  def isFuture(value:Date)=value.after(new Date)


  def movieInfo(id:Int): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj("movie_id" -> id))  // searching by a particular field
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureMovieList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureMovieList.map { movie =>
      Ok(views.html.movieInfoPage(movie.head))
    }
  }

  def seeAddMovie=Action{
    Ok(views.html.addMovie(Movie.createMovie))
  }



  //adds Movies to database
  def addMovie=Action{ implicit request=>
    val formValidationResult= Movie.createMovie.bindFromRequest
    formValidationResult.fold({
      errors =>
        println(formValidationResult)

        BadRequest(views.html.addMovie(errors))
    },{movie=> val futureResult = moviecollection.flatMap(_.insert(movie))

      futureResult.map(_ => Ok("Added user " + movie.title + " " + movie.genres))
      Redirect(routes.MongoDBController.listMovies())
    })
  }

  //Display all showings available to book
  def showingsView: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    var showingsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    showingsList.map { showing =>

      Ok(views.html.showings(showing))
    }
  }

//Select tickets types and quantity
  def ticketSelection(id:Int)=Action{
   Ok(views.html.ticketselection(id)).withSession("id"->s"${id}")
  }


  //display grid for selecting seats
  def seating: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    var seatsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    seatsList.map { showing =>

      Ok(views.html.seating(showing.head))
    }
  }

}