package CoverageTests

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {


  "Application" should {
    //    "send 404 on a bad request" in new WithApplication{route(FakeRequest(GET, "/boum")) must beNone}

    "render the index page" in new WithApplication {
      val home = route(FakeApplication(), FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("QA Cinema")
    }
    "render the 404 page" in new WithApplication {
      val notfound = route(FakeApplication(), FakeRequest(GET, "/404")).get
      status(notfound) must equalTo(NOT_FOUND)
      contentType(notfound) must beSome.which(_ == "text/html")
      contentAsString(notfound) must contain("This is not the page you are looking for.")
    }

    "render the 500 page" in new WithApplication {
      val badrequest = route(FakeApplication(), FakeRequest(GET, "/500")).get
      status(badrequest) must equalTo(OK)
      contentType(badrequest) must beSome.which(_ == "text/html")
      contentAsString(badrequest) must contain("does not exist.")
    }

    "render the Listings page" in new WithApplication {
      val listings = route(FakeApplication(), FakeRequest(GET, "/listings")).get
      status(listings) must equalTo(OK)
      contentType(listings) must beSome.which(_ == "text/html")
      contentAsString(listings) must contain("Filter Movies")
    }

    "render the Classifications page" in new WithApplication {
      val classifications = route(FakeApplication(), FakeRequest(GET, "/classifications")).get
      status(classifications) must equalTo(OK)
      contentType(classifications) must beSome.which(_ == "text/html")
      contentAsString(classifications) must contain("Parental Guidance")
    }

    "render the Contact page" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/contact")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("You can contact us using the form to the right")
    }

    "render the About us page" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/contact")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("QACinema is an independent Cinema located in Manchester")
    }

    "render the 404 page for an non existing page" in new WithApplication {
      val notfound = route(FakeApplication(), FakeRequest(GET, "/thispagedoesntexist")).get
      status(notfound) must equalTo(NOT_FOUND)
      contentType(notfound) must beSome.which(_ == "text/html")
      contentAsString(notfound) must contain("This is not the page you are looking for.")
    }

    "render the 500 page" in new WithApplication {
      val badrequest = route(FakeApplication(), FakeRequest(GET, "/movieinfo?id=1337")).get
      status(badrequest) must equalTo(OK)
      contentType(badrequest) must beSome.which(_ == "text/html")
      contentAsString(badrequest) must contain("does not exist.")
    }


    "render the local info page" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/localStuff")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Local Amenities")
    }

    "render the Find Us page" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/findUs")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Find Us")
    }

    "render the movie info page for a given movie" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/movieinfo?id=2")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Deadpool")
    }

    "render the movie info page for a given movie" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/movieinfo?id=20")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("No Such Movie")
    }

    "render the admin page with admin session" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/admin").withSession("admin" -> "admin")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Update or Delete a Movie")
    }

    "render the admin page with admin session" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/admin").withSession("admin" -> "admin")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Update or Delete a Movie")
    }

    "render the add movie page with admin session" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/addMovie").withSession("admin" -> "admin")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("movieID")
    }

    "render the upadate movie page" in new WithApplication {
      val contact = route(FakeApplication(), FakeRequest(GET, "/updateMovie").withSession("admin" -> "admin")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Movies")
    }

    "check if session exists for admin" in new WithApplication {
      val testRequest = route(FakeApplication(), FakeRequest(GET, "/sessionIn")).get
      session(testRequest).get("admin") must equalTo(Some("admin"))
    }

    "lose session" in new WithApplication {
      val testRequest = route(FakeApplication(), FakeRequest(GET, "/sessionOut").withSession("admin" -> "admin")).get
      session(testRequest).get("admin") must equalTo(None)

    }

    "Ticket selection" in new WithApplication {
      val seating = route(FakeApplication(), FakeRequest(GET, "/ticketselection?id=2")).get
      //.withSession("id" -> "2")).get
      status(seating) must equalTo(OK)
      contentType(seating) must beSome.which(_ == "text/html")
      contentAsString(seating) must contain("Tickets")
      contentAsString(seating) must contain("Select Ticket Type and Quantity")
      contentAsString(seating) must contain("Adult")
      contentAsString(seating) must contain("Child")
      contentAsString(seating) must contain("Concession")
      contentAsString(seating) must contain("Total")
      contentAsString(seating) must contain("Reserve Seats")
    }

    "Seating plan" in new WithApplication {
      val seating = route(FakeApplication(), FakeRequest(GET, "/seating?total=15&adult=1&child=0&concession=0&seatsNo=1").withSession("id" -> "2")).get
      status(seating) must equalTo(OK)
      contentType(seating) must beSome.which(_ == "text/html")
      contentAsString(seating) must contain("Submit Seats")
      contentAsString(seating) must contain("Row 1")
      contentAsString(seating) must contain("Row 2")
      contentAsString(seating) must contain("Row 3")
      contentAsString(seating) must contain("Row 4")
      contentAsString(seating) must contain("Row 5")
      contentAsString(seating) must contain("Seats Selected:")
    }

    "Seating plan" in new WithApplication {
      val seating = route(FakeApplication(), FakeRequest(GET, "/userinfo?seats=,0%200").withSession("id" -> "2")).get
      status(seating) must equalTo(OK)
      contentType(seating) must beSome.which(_ == "text/html")
      contentAsString(seating) must contain("Tickets")
      contentAsString(seating) must contain("Enter user details")
      contentAsString(seating) must contain("Submit")
    }


    "send Bad on bad" in new WithApplication() {
      val mailResult = route(FakeApplication(), FakeRequest(GET,
        "/contactform?Name=hello&Email=Hi&Subject=Stuff&Message=Waa%21&sub")).orNull
      status(mailResult) must equalTo(OK)
      contentType(mailResult) must beSome.which(_ == "text/html")
      contentAsString(mailResult) must contain("enter an email")

    }
    "send 303 on success" in new WithApplication() {
      val mailResult = route(FakeApplication(), FakeRequest(GET,
        "/contactform?Name=hello&Email=daniel.raineri@qa.com&Subject=Stuff&Message=Waa&submit=")).orNull
      status(mailResult) must equalTo(SEE_OTHER)
    }

    "add movie to database" in new WithApplication() {
      val addMovieResult = route(FakeApplication(), FakeRequest(POST,
        "/addMovie?movie_id=10&title=leaveThisForNow&poster_path=somepath&landscape=Stuff&video=aaa&images=asdasd&age_rating=12A&" +
          "user_rating=2.5&release_date=2017-06-08&run_time=123&genres=asda&overview=aaa&cast=asdas&director=bbb&screen=dsada&21&submit=").withSession("admin"->"admin")).orNull
      status(addMovieResult) must equalTo(303)
    }

    "bad request add movie to database" in new WithApplication() {
      val addMovieResult = route(FakeApplication(), FakeRequest(POST,
        "/addMovie?movie_id=2&title=asda&poster_path=somepath&landscape=Stuff&video=aaa&images=asdasd&age_rating=dsad&" +
          "user_rating=fail.5&release_date=asdas&run_time=123&genres=asda&overview=aaa&cast=asdas&director=bbb&screen=dsada&submit=").withSession("admin"->"admin")).orNull
      status(addMovieResult) must equalTo(400)
    }

//    "update movie" in new WithApplication() {
//      val updateMovieResult = route(FakeApplication(), FakeRequest(POST,
//        "/updateMovie/10?movie_id=2&title=asda&poster_path=somepath&landscape=Stuff&video=aaa&images=asdasd&age_rating=dsad&" +
//          "user_rating=fail.5&release_date=asdas&run_time=123&genres=asda&overview=aaa&cast=asdas&director=bbb&screen=dsada&submit=").withSession("admin"->"admin")).orNull
//      status(updateMovieResult) must equalTo(303)
//    }






  }
}
//movie_id: Int,
//title: String,
//poster_path: String,
//landscape:String,
//video:String,
//images:String,
//var age_rating:String,
//user_rating:BigDecimal,
//release_date:String,
//run_time:Int,
//genres:String,
//overview:String,
//cast:String,
//director:String,
//screen:String