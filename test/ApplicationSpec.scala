import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.mvc._
import play.api.test.Helpers._

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
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("QA Cinema")
    }
    "render the 404 page" in new WithApplication {
      val notfound = route(FakeRequest(GET, "/404")).get
      status(notfound) must equalTo(NOT_FOUND)
      contentType(notfound) must beSome.which(_ == "text/html")
      contentAsString(notfound) must contain("This is not the page you are looking for.")
    }

    "render the 500 page" in new WithApplication {
      val badrequest = route(FakeRequest(GET, "/500")).get
      status(badrequest) must equalTo(OK)
      contentType(badrequest) must beSome.which(_ == "text/html")
      contentAsString(badrequest) must contain("does not exist.")
    }

    "render the Listings page" in new WithApplication {
      val listings = route(FakeRequest(GET, "/listings")).get
      status(listings) must equalTo(OK)
      contentType(listings) must beSome.which(_ == "text/html")
      contentAsString(listings) must contain("Filter Movies")
    }

    "render the Classifications page" in new WithApplication {
      val classifications = route(FakeRequest(GET, "/classifications")).get
      status(classifications) must equalTo(OK)
      contentType(classifications) must beSome.which(_ == "text/html")
      contentAsString(classifications) must contain("Parental Guidance")
    }

    "render the Contact page" in new WithApplication {
      val contact = route(FakeRequest(GET, "/contact")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("Please do not hesitate to get in contact with us.")
    }

    "render the About us page" in new WithApplication {
      val contact = route(FakeRequest(GET, "/contact")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain("QACinema is an independent Cinema located in Manchester")
    }

    "render the 404 page for an non existing page" in new WithApplication {
      val notfound = route(FakeRequest(GET, "/thispagedoesntexist")).get
      status(notfound) must equalTo(NOT_FOUND)
      contentType(notfound) must beSome.which(_ == "text/html")
      contentAsString(notfound) must contain("This is not the page you are looking for.")
    }

    "render the 500 page" in new WithApplication {
      val badrequest = route(FakeRequest(GET, "/movieinfo?id=1337")).get
      status(badrequest) must equalTo(OK)
      contentType(badrequest) must beSome.which(_ == "text/html")
      contentAsString(badrequest) must contain("does not exist.")
    }

  }
}
