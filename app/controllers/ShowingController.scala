package controllers

import java.sql.Timestamp
import java.util.{Calendar, Date}
import javax.inject.Inject

import scala.util.Try
import scala.concurrent.{Await, Future}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._
import play.api.i18n._
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration.Duration


class ShowingController @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with I18nSupport {


  def showings: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("showings"))

  //Display all showings available to book
  def showingsView(movieTitle: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }

    val showingsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    showingsList.map { showing =>
      if(movieTitle!="/all") {
        val newShowings = showing.filter(elem => elem.movieId == movieTitle)
        if(newShowings.nonEmpty) {
          Ok(views.html.showings(newShowings))
        }
        else {
          Ok(views.html.showings(showing))
        }
      }
      else{
        Ok(views.html.showings(showing))
      }
    }
  }


}