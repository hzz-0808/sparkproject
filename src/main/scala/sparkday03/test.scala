package sparkday03

import java.text.SimpleDateFormat
import java.util.Date

object test {

  def main(args: Array[String]): Unit = {
    val hourstr = "1516609143867"
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH")
    val formatHour: String = sdf.format(new Date(hourstr.toLong))
    println(formatHour)

    val time = "20160327082400"

    val timestample: Long = new Date(time).getTime

    println(timestample)
  }

}
