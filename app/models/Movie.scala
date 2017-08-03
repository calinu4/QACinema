package models


case class Movie(
                 movie_id: Int,
                 title: String,
                 poster_path: String,
                 landscape:String,
                 video:String,
                 images:List[String],
                 var age_rating:String,
                 user_rating:Float,
                 release_date:String,
                 run_time:Int,
                 genres:List[String],
                 overview:String,
                 cast:String,
                 director:String,
                 screen:List[String]

               )


object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Movie thanks to Json Macros
  implicit val movieFormat = Json.format[Movie]
}


