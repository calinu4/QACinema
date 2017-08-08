package models

import scala.collection.mutable.MutableList


case class Showing(
                  showingId:Int,
                  roomId:Int,
                  movieId:String,
                  date:String,
                  time:String,
                  var seatsAvailable:Int,
                  var seats:List[List[Int]]
                  )


