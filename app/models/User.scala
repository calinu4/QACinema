package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json


case class User(
                  email: String,
                  password: String,
                  profile: String
                )

object User {
  implicit val UserFormat = Json.format[User]
  val createLoginForm = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "profile" -> text
    )(User.apply)(User.unapply))
}