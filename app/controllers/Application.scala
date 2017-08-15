package controllers

import models.ContactUs
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api._
import play.api.mvc._
import java.io._

class Application extends Controller {



  def contact = Action {
    Ok(views.html.contact("Contact Us",ContactUs.createContactForm))
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


  def sessionIn() = Action {implicit request =>
    request.session.get("admin").map { user =>
      Ok(views.html.AdminControllerPage("Admin Page"))
    }.getOrElse {
      Ok(views.html.messagePage("Logged in")).withSession("admin" -> "admin")
    }
  }

  def sessionOut() = Action {
    Ok(views.html.messagePage("You are logged out")).withNewSession
  }

}
