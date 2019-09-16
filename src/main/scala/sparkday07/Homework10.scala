package sparkday07

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}


case class ip(start_num:Long,end_num:Long,local:String)
case class order(ip:String,dip:Long,good:String)
object Homework10 {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("udf")
      .master("local[*]")
      .getOrCreate()
    import  org.apache.spark.sql.functions._
    import sparkSession.implicits._
    val ipds: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day02\\ip.txt")
    val datads: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day02\\order.log")

    val ds: Dataset[ip] = ipds.map(x => {
      val arr: Array[String] = x.split("\\|").filter(_ != "")
      //println(arr.toBuffer)
      ip(arr(2).toLong, arr(3).toLong, arr(4) + arr(5) + arr(6) + arr(7))
    })
    val df1: DataFrame = ds.toDF()
    val ds2: Dataset[order] = datads.map(x => {
      val arr: Array[String] = x.split(" ")
      order(arr(1),ip2Long(arr(1)),arr(3))
    })
    val df2: DataFrame = ds2.toDF()

    val res: DataFrame = df2.join(df1,expr("dip>start_num and dip<end_num"))
    res.show()
    sparkSession.stop()


  }
  def ip2Long(ip:String):Long = {
    val fragments: Array[String] = ip.split("\\.")
    val res = fragments(0).toLong*16777216+fragments(1).toLong*65536+fragments(2).toLong*256+fragments(3).toLong*1
    res
  }

  //  def power2(x:Int):Long={
//    if(x==0)
//       1
//    else
//      2*power2(x-1)
//  }
}
