package models

/**
  * Created by Administrator on 03/08/2017.
  */
case class Reservation(
                      name:String,
                      email:String,
                      adult:Int,
                      child:Int,
                      concession:Int,
                      seats:List[List[String]],
                      total:Int
                      )


