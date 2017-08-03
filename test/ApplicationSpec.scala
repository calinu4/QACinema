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

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("QA Cinema")
    }

    "render the Listings page" in new WithApplication{
      val listings = route(FakeRequest(GET, "/listings")).get
      status(listings) must equalTo(OK)
      contentType(listings) must beSome.which(_ == "text/html")
      contentAsString(listings) must contain ("Filter Movies")
    }
    "render the Contact page" in new WithApplication{
      val contact = route(FakeRequest(GET, "/contact")).get
      status(contact) must equalTo(OK)
      contentType(contact) must beSome.which(_ == "text/html")
      contentAsString(contact) must contain ("Please do not hesitate to get")
    }
    "render the Classifications page" in new WithApplication{
        val listings = route(FakeRequest(GET, "/classifications")).get
        status(listings) must equalTo(OK)
        contentType(listings) must beSome.which(_ == "text/html")
        contentAsString(listings) must contain ("Parental Guidance")
    }
    "render the 404 page" in new WithApplication{
      val listings = route(FakeRequest(GET, "/404")).get
      status(listings) must equalTo(NOT_FOUND)
      contentType(listings) must beSome.which(_ == "text/html")
      contentAsString(listings) must contain ("This is not the page you are looking for.")
    }
    "render the 500 page" in new WithApplication{
      val listings = route(FakeRequest(GET, "/500")).get
      status(listings) must equalTo(OK)
      contentType(listings) must beSome.which(_ == "text/html")
      contentAsString(listings) must contain ("does not exist.")
    }
  }
}
