package controllers

import javax.inject.Inject

import models.ContactUs
import org.apache.commons.mail.EmailException
import play.api.mvc._
import play.api.libs.mailer._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class MailController @Inject()(mailerClient: MailerClient) extends Controller {

  def getFormContent =  Action { implicit request =>
    try{
      val formValidationResult = ContactUs.createContactForm.bindFromRequest
      formValidationResult.fold({ formWithErrors =>
        BadRequest(views.html.contact("error", formWithErrors))
      }, { contact =>
        sendEmail(contact)
        Redirect(routes.Application.contact)
      })
      }catch{
      case email : EmailException => Ok(views.html.messagePage("You didn't enter an email."))
    }
  }

  def sendEmail(contact: ContactUs) = {
    val email = Email(
      "Contact Form: " + contact.subject,
       contact.name +  " <" + contact.email + ">",
      Seq("QACinema ContactFormResponse <qacinemauk+contact@gmail.com>"),
      bodyText = Some("Message from: " + contact.name +
            "\n\n Email Address: " + contact.email +
            "\n\n Subject: " + contact.subject +
            "\n\n Message: " + contact.message)
    )
    mailerClient.send(email)
  }
}