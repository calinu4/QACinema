package models

/**
  * Created by Administrator on 03/08/2017.
  */
case class Reservation(
                      name:String,
                      email:String,
                      adult:Int,
                      child:Int,
                      seats:List[List[Int]],
                      total:Int
                      )


