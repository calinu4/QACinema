package models


case class Movie(
                 movie_id: Int,
                 title: String,
                 poster_path: String,
                 var age_rating:String,
                 user_rating:String,
                 release_date:String,
                 run_time:Int,
                 genre_ids:List[Int],
                 overview:String,
                 cast:String,
                 director:String,
                 screen:List[Int]

               )


object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val genreFormat = Json.format[Genre]
  implicit val screenFormat = Json.format[Screen]
  implicit val movieFormat = Json.format[Movie]
}


