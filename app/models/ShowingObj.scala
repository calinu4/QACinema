package models

import play.api.data.Form
import play.api.data.Forms._


case class ShowingObj(
                    showingId:Int,
                    roomId:Int,
                    movieId:String,
                    date:String,
                    time:String
                  )

object ShowingObj{



  val createShowing = Form(
    mapping(
      "showingId"->number,
      "roomId"->number,
      "movieId"->nonEmptyText,
      "date"->nonEmptyText,
      "time"->nonEmptyText
    )(ShowingObj.apply)(ShowingObj.unapply))

}

