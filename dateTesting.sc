import java.util.Calendar

val now = Calendar.getInstance().toInstant

val currentDate = now.toString.splitAt(10)._1

val currentTime = now.toString.substring(11,16)