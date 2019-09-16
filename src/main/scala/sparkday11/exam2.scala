package sparkday11

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

//计算该企业2016年股票周平均收盘价格（20）。
case class GuPiao(year:String,week:Int,price:Double)
object exam2 {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("udf")
      .master("local[*]")
      .getOrCreate()
    import  org.apache.spark.sql.functions._
    import sparkSession.implicits._
    val src: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day02\\bb.txt")

    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val calendar: Calendar = Calendar.getInstance()
    val mapeds: Dataset[GuPiao] = src.map(line => {

      val fields: Array[String] = line.split(",")
      val ds = fields(0).split("-")
      val date: Date = sdf.parse(fields(0))
      calendar.setTime(date)
      val week: Int = calendar.getWeeksInWeekYear
      println(week)
      GuPiao(ds(0), week,fields(4).toDouble)

    })
    val frame: DataFrame = mapeds.toDF()
    val res: Dataset[Row] = frame.where("year='2016'").groupBy("week").agg(avg("price"))
    res.show()
    sparkSession.stop()

  }

}
