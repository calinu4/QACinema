package controllers

import play.api._
import play.api.mvc._
import java.io._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Home"))
  }

  def contact = Action {
    Ok(views.html.contact("Contact"))
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

  def payment = Action {
    //the price in there that you want the checkout button to have
    Ok(views.html.payment("2.50"))
  }
}
