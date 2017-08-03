package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json


case class Movie(
                 movie_id: Int,
                 title: String,
                 poster_path: String,
                 landscape:String,
                 video:String,
                 images:List[String],
                 var age_rating:String,
                 user_rating:BigDecimal,
                 release_date:String,
                 run_time:Int,
                 genres:List[String],
                 overview:String,
                 cast:String,
                 director:String,
                 screen:List[String]

               )

object Movie{
  implicit val movieFormat = Json.format[Movie]

  val createMovie = Form(
   mapping(
     "movie_id"->number,
    "title"->nonEmptyText,
     "poster_path"->nonEmptyText,
     "landscape"->nonEmptyText,
     "video"->nonEmptyText,
     "images"->list(nonEmptyText),
    "age_rating"->nonEmptyText,
    "user_rating"->bigDecimal,
    "release_date"->nonEmptyText,
    "run_time"->number,
    "genres"->list(nonEmptyText),
    "overview"->nonEmptyText,
    "cast"->nonEmptyText,
    "director"->nonEmptyText,
    "screen"->list(nonEmptyText)

  )(Movie.apply)(Movie.unapply))
}
object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Movie thanks to Json Macros
  implicit val movieFormat = Json.format[Movie]
}


