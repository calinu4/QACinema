package models

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class ContactUs (

    name: String,
    email: String,
    subject: String,
    message: String
)

object ContactUs {

  val createContactForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Email" -> nonEmptyText,
      "Subject" -> of(stringFormat),
      "Message" -> nonEmptyText
    )(ContactUs.apply)(ContactUs.unapply)
  )

}
