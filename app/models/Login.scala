package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.libs.mailer.Email
import sun.security.util.Password

case class Login(
                  email: String,
                  password: String
                )

object Login {
  implicit val LoginFormat = Json.format[Login]
  val createLoginForm = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply))
}