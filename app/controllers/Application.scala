package controllers

import models._
import play.api.Play.current
import play.api.i18n.Messages._
import play.api._
import play.api.mvc._
import java.io._
import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi, Messages}
import play.api.libs.json
import play.api.libs.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.play.json.collection.JSONCollection
import views.html.helper.form

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection._

class Application @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with I18nSupport {


  def contact = Action {
    Ok(views.html.contact("Contact Us", ContactUs.createContactForm))
  }

  def classification = Action {
    Ok(views.html.classification("Classifications"))
  }

  def localInfo = Action {

    Ok(views.html.localInfo("Local Amenities"))

  }

  def brokenLink = Action { implicit request =>
    NotFound(views.html.notFound())
  }

  def noSuchMovie = Action { implicit request =>
    Ok(views.html.noSuchMovie(0))
  }

  def findUs = Action {
    Ok(views.html.findUs("Find Us"))
  }


  def screenInformation = Action {
    Ok(views.html.screenInformation("Screen Information"))
  }


  def sessionIn() = Action { implicit request =>
    request.session.get("admin").map { user =>
      Ok(views.html.AdminControllerPage("Admin Page"))
    }.getOrElse {
      Ok(views.html.messagePage("Logged in")).withSession("admin" -> "admin")
    }
  }

  def sessionOut() = Action {
    Ok(views.html.messagePage("You are logged out")).withNewSession
  }

  def session() = Action { implicit request =>
    request.session.get("admin").map { user =>
      Ok(views.html.messagePage("You are Logged in! " + user))
        Redirect(routes.MongoDBController.listIndexMovies())


    }.getOrElse {
      Unauthorized(views.html.messagePage("You are not logged in!"))
    }
  }

  def loginGet() = Action { implicit request =>
    Ok(views.html.login(Login.createLoginForm))
  }


  val usersResult = database.map(_.collection[JSONCollection]("users"))

  def getUsers(): List[User] = {
    val cursor: Future[Cursor[User]] = usersResult.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[User]
    }
    val futureResList: Future[List[User]] = cursor.flatMap(_.collect[List]())
    Await.result(futureResList, Duration.Inf)
  }


  def login()= Action { implicit request =>
    //println(request.body.asJson.foreach(value => println("##### " + value.toString().split(",")(0).split(":")(1))))

    val formLoginRequest = Login.createLoginForm.bindFromRequest
    formLoginRequest.fold({
      errors =>
        println(formLoginRequest)
        BadRequest(views.html.login(errors))

    }, { login =>
      println(login.email + login.password)
      val users = getUsers()
      var user =""
      println(users.mkString)
      var isFound=false
      users.foreach(u => if (u.email == login.email && u.password == login.password){
        isFound = true
        user = u.admin
      println(u.admin)}else {})
      if(isFound){
          Redirect(routes.MongoDBController.listIndexMovies()).withSession(request.session + ("email" -> login.email) + ("admin" -> user))

        //Ok(views.html.index).withSession("email" -> login.email)
      }else{
        Redirect(routes.Application.loginGet())
      }
    })
  }
}
