import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

  val host = "http://localhost:9000/"

  "The 404 page" should {
    "have the correct title" in new WithBrowser {
      browser.goTo(host+"404")
      browser.title mustEqual("404: Page Not Found")
    }
  }
}
