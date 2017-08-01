package controllers

import play.api._
import play.api.mvc._
import java.io._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Home"))
  }

/*
  def listnings = Action{
    Ok(views.html.listnings("Listnings"))
  }*/
}