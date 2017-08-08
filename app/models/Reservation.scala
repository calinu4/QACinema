package models


case class Reservation(
                      reservationId:String,
                      name:String,
                      email:String,
                      adult:Int,
                      child:Int,
                      concession:Int,
                      seats:List[List[String]],
                      total:Int,
                      movie:String,
                      date:String,
                      time:String,
                      room:String,
                      paid:Boolean
                      )


