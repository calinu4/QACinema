package controllers

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
      Ok(views.html.listnings(movies))
    }
  }

  def replaceAgeRating(age: String): String = {
    age match {
      case "12" => "https://jonkuhrt.files.wordpress.com/2014/01/bbfc_12_rating1.png"
      case "12A" => "https://userscontent2.emaze.com/images/076748b8-6db0-4f13-879b-84d05d4ef68f/7799a9d75c014ad46716a1d53a52edfd.png"
      case _ => "https://userscontent2.emaze.com/images/076748b8-6db0-4f13-879b-84d05d4ef68f/7799a9d75c014ad46716a1d53a52edfd.png"

    }
  }




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


}