//import org.scalatest.FlatSpec
//import controllers.MongoDBController
//import play.api.i18n.{I18nSupport, Lang, Messages, MessagesApi}
//import play.api.mvc.{RequestHeader, Result}
//import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
//import play.mvc.Http
//import models.{JsonFormats, Movie, Showing}
//import play.api.test.FakeRequest
//import play.api.test.Helpers.{GET, POST, route}
//import org.scalatest._
//import org.scalatest.mock.MockitoSugar
//import org.scalatestplus.play._
//import org.mockito.Mockito._
//import play.api.i18n.MessagesApi
//import reactivemongo.api.DefaultDB
//import reactivemongo.play.json.collection.JSONCollection
//
//import scala.concurrent.Future
//
//
//
//class InsideTestsFlatSpec extends FlatSpec with MockitoSugar {
//  var movie = new Movie(2, "actionFilm", "posterUrl", "landscape", "videoURL", "imagesURL",
//    "12A", 7.1, "1998/01/23", 72, "action", "long overview text",
//    "actors apparently", "director", "screen"
//  )
//
//
//
//
//
//
//  "An empty Set" should "have size 0" in {
////    val mockMongoController = mock[MongoDBController]
////    val mockMessages = mock[MessagesApi]
////    val mockReactive = mock[ReactiveMongoApi]
////    val mockFuture = mock[Future[JSONCollection]]
////    val mockFutureDB = mock[DefaultDB]
////
////    val array = new Array[Array[Int]](32)
////    when(mockMongoController.moviecollection) thenReturn mockFuture
////    when(mockMongoController.getMovies) thenReturn List(new Movie(1, "", "", "", "", "", "", 2, "",2, "", "", "", "", ""))
////    val c = new MongoDBController(mockMessages)(mockReactive)
////    println("################ " + c.getMovies())
//
////    movie_id: Int,
////    title: String,
////    poster_path: String,
////    landscape:String,
////    video:String,
////    images:String,
////    var age_rating:String,
////    user_rating:BigDecimal,
////    release_date:String,
////    run_time:Int,
////    genres:String,
////    overview:String,
////    cast:String,
////
////    screen:String
////    director:String,
//
//    assert(Set.empty.size == 0)
//  }
////case class Showing(
////  showingId:Int,
////  roomId:Int,
////  movieId:String,
////  date:String,
////  time:String,
////  var seatsAvailable:Int,
////  var seats:Array[Array[Int]]
////  )
//  it should "produce NoSuchElementException when head is invoked" in {
//    assertThrows[NoSuchElementException] {
//      Set.empty.head
//    }
//  }
//}
//
//
//
//
