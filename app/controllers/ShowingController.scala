package controllers

import java.sql.Timestamp
import java.time.LocalDate
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

  def dateParse(date: String): Date = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    format.parse(date)
  }

  def isFuture(value: Date): Boolean = value.after(new Date)
  def isToday(value: String): Boolean = {
    val now = Calendar.getInstance().toInstant
    val currentDate = now.toString.splitAt(10)._1
    if(currentDate==value)
      true
    else
      false
  }

  def showings: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("showings"))

  def getShowings: List[Showing] = {
    val cursor: Future[Cursor[Showing]] = showings.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Showing]
    }
    val futureResList: Future[List[Showing]] = cursor.flatMap(_.collect[List]())
    Await.result(futureResList, Duration.Inf)
  }

  //Display all showings available to book
  def showingsView(movieTitle: String, date: String): Action[AnyContent] = Action {
    val sevenDays = getSevenDays

    val showingsList = getShowings
    if (movieTitle != "/all") {
      val newShowings = showingsList.filter(elem => elem.movieId == movieTitle).filter(elem=>isToday(elem.date)||isFuture(dateParse(elem.date)))
      if (newShowings.nonEmpty) {
        Ok(views.html.showings(newShowings, sevenDays,date.toInt-1,false))
      }
      else {
        Ok(views.html.showings(showingsList, sevenDays,date.toInt-1,true))
      }
    }
    else {
      val filteredShowings = filterShowingByDate(date, showingsList)
      Ok(views.html.showings(filteredShowings, sevenDays,date.toInt-1,true))
    }
  }

  def filterShowingByDate(date: String, showings: List[Showing]): List[Showing] = {
    val sevenDays = getSevenDays
    date match {
      case "1" => showings.filter(e => e.date == sevenDays(0))
      case "2" => showings.filter(e => e.date == sevenDays(1))
      case "3" => showings.filter(e => e.date == sevenDays(2))
      case "4" => showings.filter(e => e.date == sevenDays(3))
      case "5" => showings.filter(e => e.date == sevenDays(4))
      case "6" => showings.filter(e => e.date == sevenDays(5))
      case "7" => showings.filter(e => e.date == sevenDays(6))
      case _ => showings
    }
  }

  //Added 7 days list here
  def incrementDayByOne(now: String): String = LocalDate.parse(now).plusDays(1).toString

  def getSevenDays: Array[String] = {
    val now = Calendar.getInstance().toInstant
    val currentDate = now.toString.splitAt(10)._1
    val currentTime = now.toString.substring(11, 16)
    val datesList: Array[String] = new Array[String](8)
    datesList(0) = currentDate

    for (i <- 1 until 7) {
      datesList(i) = incrementDayByOne(datesList(i - 1))
    }
    datesList
  }



}