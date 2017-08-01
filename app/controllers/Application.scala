package controllers

import play.api._
import play.api.mvc._
import java.io._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Home"))
  }

}
//Scott branch