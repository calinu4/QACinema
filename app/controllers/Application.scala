package controllers

import play.api._
import play.api.mvc._
import java.io._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Home"))
  }

  def bookingConfirmation = Action {
    Ok(views.html.bookingConfirmation())
  }

  def write = Action {
    writeToFile()
    Ok.sendFile(new java.io.File("hello.txt"), inline = false)
  }

  def writeToFile(): Unit = {
    val pw = new PrintWriter(new File("hello.txt"))
    pw.write("Hello, world")
    pw.close()
  }

  def delete = Action {
    deleteFile()
    Redirect(routes.Application.bookingConfirmation())
  }

  def deleteFile(): Unit = {
    new File("hello.txt").delete()
  }

}