package controllers

import models.ContactUs
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api._
import play.api.mvc._
import java.io._


class Application extends Controller {



  def contact = Action {
    implicit request =>
    Ok(views.html.contact("Contact", ContactUs.createContactForm))
  }

  def classification = Action {
    Ok(views.html.classification("Classifications"))
  }

  def localStuff = Action {

    Ok(views.html.localStuff("Local Amenities"))

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


  def sessionIn() = Action {
    Ok(views.html.messagePage("Logged in")).withSession("admin" -> "admin")
  }

  def sessionOut() = Action {
    Ok(views.html.messagePage("You are logged out")).withNewSession
  }

  def session() = Action { implicit request =>
    request.session.get("admin").map { user =>
      Ok(views.html.messagePage("You are Logged in! "+user))
    }.getOrElse {
      Unauthorized(views.html.messagePage("You are not logged in!"))
    }
  }



}
