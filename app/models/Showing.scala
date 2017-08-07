package models


case class Showing(
                  sessionId:Int,
                  roomId:Int,
                  movieId:String,
                  date:String,
                  time:String,
                  seatsAvailable:Int,
                  seats:Array[Array[Int]],
                  reservations:List[Reservation]
                  )


