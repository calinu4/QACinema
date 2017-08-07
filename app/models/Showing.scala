package models

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


case class Showing(
                  showingId:Int,
                  roomId:Int,
                  movieId:String,
                  date:String,
                  time:String,
                  var seatsAvailable:Int,
                  seats:ArrayBuffer[ArrayBuffer[Int]],
                  reservations:ListBuffer[Reservation]
                  )


