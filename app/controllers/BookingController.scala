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


class BookingController @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with I18nSupport {


  def showings: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("showings"))

  def getshowing(showingId: Int): Showing = {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj("showingId" -> showingId))
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val futureResList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    Await.result(futureResList, Duration.Inf).head
  }



  def dateParse(date: String): Date = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    format.parse(date)
  }

  def isFuture(value: Date): Boolean = value.after(new Date)

  def isCurrent(value: Date): Boolean = value.before(new Date)

  //Display all showings available to book
  def individualView(id:String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val showingsList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    showingsList.map { showing =>
      val individualShowings=showing.filter(p=>p.movieId==id)
      Ok(views.html.showings(individualShowings))
    }
  }







}