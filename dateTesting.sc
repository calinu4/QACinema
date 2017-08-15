import java.time.LocalDate
import java.util.Calendar


def incrementDayByOne(now:String): String = {
  return LocalDate.parse(now).plusDays(1).toString
}

def getSevenDays():Array[String]={
  val now = Calendar.getInstance().toInstant
  val currentDate = now.toString.splitAt(10)._1
  val currentTime = now.toString.substring(11,16)
  val datesList:Array[String]=new Array[String](7)
  datesList(0)=currentDate

  for(i<-1 until 7)
    datesList(i)=incrementDayByOne(datesList(i-1))
  datesList
}

val result=getSevenDays().foreach(e=>println(e))