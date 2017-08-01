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

  // TODO - keep in mind you need to have mongod.exe running before attempting to play around
  //Read from table persons
  def moviecollection: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("movies"))

  def genrescollection: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("genres"))

  def screenscollection: Future[JSONCollection] = database.map(
    _.collection[JSONCollection]("screens"))

  //display Movies from database
  def listMovies:Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Movie]] = moviecollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Movie]
    }
    val futureUsersList: Future[List[Movie]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { movies =>
      Ok(views.html.listings(movies))
    }
  }

  //display Screens from database
  def listScreens:Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Screen]] = screenscollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Screen]
    }
    val futureScreensList: Future[List[Screen]] = cursor.flatMap(_.collect[List]())
    futureScreensList.map { screens =>
      Ok(views.html.screens(screens))
    }
  }

  //
  def listGenres:Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Genre]] = genrescollection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[Genre]
    }
    val futureScreensList: Future[List[Genre]] = cursor.flatMap(_.collect[List]())
    futureScreensList.map { genres =>
      Ok(views.html.genres(genres))
    }
  }










}