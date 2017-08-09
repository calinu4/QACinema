import org.junit.runner._
import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.FlatSpec
import org.scalatest.selenium.WebBrowser
import org.specs2.matcher.ShouldMatchers
import org.specs2.runner._

/**
  * add your integration spec here.
  * An integration test will fire up a whole play application in a real (or headless) browser
  */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends FlatSpec with ShouldMatchers with WebBrowser {

  implicit val webDriver: WebDriver = new HtmlUnitDriver

  val host = "http://localhost:9000/"

  "The website navigation" should "navigate through booking to the seat selection page" in {
    go to (host + "showings")
    click on cssSelector("#\\31 ")
    singleSel("adult").value = "2"
    singleSel("adult").value shouldEqual "2"
    println(singleSel("adult").value)

    webDriver.findElement(By.xpath("/html/body/div[1]/div/div/button")).click()
    pageTitle shouldEqual "Seating Plan"
  }

  "The blog app home page" should "have the correct title" in {
    go to host
    pageTitle shouldEqual "QA Cinema"
  }

  it should "contain a \"View All\" link which redrects to the Listings page" in {
    find(xpath("/html/body/div[1]/div/div[2]/h2/a")) shouldEqual ('displayed)
  }

  "The directions search bar" should "redirect to the correct Google Maps page" in {
    go to (host + "findUs")
    click on name("saddr")
    textField("saddr").value = "manchester victoria"
    textField("saddr").value shouldEqual "manchester victoria"
    submit()
    pageTitle shouldEqual "Manchester Victoria to The Lowry - Google Maps"
  }

  "The admin page" should "not show CRUD when not logged in" in {
    go to (host + "admin")
    pageTitle shouldEqual "You are not logged in!"
  }

  it should "show CRUD when logged in" in {
    click on xpath("//*[@id=\"bs-example-navbar-collapse-1\"]/ul[2]/li[2]/a")
    go to (host + "admin")
    pageTitle shouldEqual "admin"
  }

}
