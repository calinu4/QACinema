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

  "The website navigation" should "navigate through booking and select two adult tickets" in {
    go to (host + "showings")
    click on cssSelector("#\\31 ")
    singleSel("adult").value = "2"
    singleSel("adult").value shouldEqual "2"
  }

  it should "go to the seat selection page" in {
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
    val loginButton = xpath("//*[@id=\"bs-example-navbar-collapse-1\"]/ul[2]/li[2]/a")
    click on loginButton
    go to (host + "admin")
    pageTitle shouldEqual "admin"
  }

  "The classifications page" should "be available from the footer on multiple pages" in {
    go to host
    click on linkText("Classifications")
    pageTitle shouldEqual "Classifications"

    go to (host + "listings")
    click on linkText("Classifications")
    pageTitle shouldEqual "Classifications"

    go to (host + "sessionIn")
    go to (host + "admin")
    click on linkText("Classifications")
    pageTitle shouldEqual "Classifications"
  }

  it should "contain links to relevant BBFC classifications pages" in {
    click on linkText("PG")
    println(pageTitle)
    pageTitle shouldEqual "PG | British Board of Film Classification"

    go to (host + "classifications")
    click on linkText("15")
    pageTitle shouldEqual "15 | British Board of Film Classification"

    go to (host + "classifications")
    click on linkText("12A")
    pageTitle shouldEqual "12A and 12 | British Board of Film Classification"

    go to (host + "classifications")
    click on linkText("18")
    pageTitle shouldEqual "18 | British Board of Film Classification"
  }

  "The 404 page" should "appear when the designated 404 link is navigated to" in {
    go to (host + "404")
    pageTitle shouldEqual "404: Page Not Found"
  }

  it should "appear when an invalid path is navigated to" in {
    go to (host + "not-a-page")
    pageTitle shouldEqual "404: Page Not Found"

    go to (host + "jshefiuhweghewgewe")
    pageTitle shouldEqual "404: Page Not Found"
  }

  "The 500 page" should "appear when an invalid movie id is entered on the individual movie page" in {
    go to (host + "movieinfo?id=456437")
    pageTitle shouldEqual "500: No Such Movie"
  }

}
