package models

import scala.collection.mutable.ArraySeq


case class Showing(
                  showingId:Int,
                  roomId:Int,
                  movieId:String,
                  date:String,
                  time:String,
                  var seatsAvailable:Int,
                  var seats:Array[Array[Int]]
                  )


