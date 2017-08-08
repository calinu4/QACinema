import org.junit.runner._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.{Seconds, Span}
import org.specs2.matcher.ShouldMatchers
import org.specs2.runner._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import play.api.test._
import controllers._
import play.api.test.Helpers._

/**
  * add your integration spec here.
  * An integration test will fire up a whole play application in a real (or headless) browser
  */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends FlatSpec with Matchers with WebBrowser {

  implicit val webDriver: WebDriver = new HtmlUnitDriver

  val host = "http://localhost:9000/"

      "The website navigation" should "navigate through booking to the seat selection page" in {
    go to (host + "showings")
    click on cssSelector("#\\31 ")
    singleSel("adult").value = "2"
    singleSel("adult").value shouldBe "2"
    println(singleSel("adult").value)
    click on id("goToSeatSelection")

    pageTitle shouldEqual "Seating Plan"
    println(pageTitle)
  }

  "The seat selection" should "throw an error when too many seats are selected" in {
    go to (host +"http://localhost:9000/seating?total=30&adult=2&child=0&concession=0&seatsNo=2")

      click on id("4 1")
  }


  "The blog app home page" should "have the correct title" in {
    go to host
    pageTitle shouldEqual "QA Cinema"
  }

  "The directions search bar" should "redirect to the correct Google Maps page" in {
    go to (host + "findUs")
    click on name("saddr")
    textField("saddr").value = "manchester victoria"
//    textField("saddr").value shouldBe "manchester victoria"
    submit()
    implicitlyWait(Span(5, Seconds))
    println(currentUrl)
    pageTitle shouldEqual "Manchester Victoria to The Lowry - Google Maps"
  }

}
