package controllers

import java.io.{File, PrintWriter}

import play.api.mvc._

import scala.concurrent.Future

class Receipt extends Controller {


  def bookingConfirmation = Action {
    Ok(views.html.bookingConfirmation())
  }

  def receipt = Action {
    val pw = new PrintWriter(new File("hello.txt"))
    pw.write("Hello, world")
    pw.close()
    Ok.sendFile(new java.io.File("hello.txt"), inline = false)
  }

}