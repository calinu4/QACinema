import org.scalatest.FlatSpec
import controllers.MongoDBController
import play.api.i18n.{I18nSupport, Lang, Messages, MessagesApi}
import play.api.mvc.{RequestHeader, Result}
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents}
import play.mvc.Http
import models.{JsonFormats, Movie}
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET,POST, route}



class InsideTestsFlatSpec extends FlatSpec {
  var movie = new Movie(2, "actionFilm", "posterUrl", "landscape", "videoURL", "imagesURL",
    "12A", 7.1, "1998/01/23", 72, "action", "long overview text",
    "actors apparently", "director", "screen"
  )






  "An empty Set" should "have size 0" in {
    assert(Set.empty.size == 0)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }
  }
}




