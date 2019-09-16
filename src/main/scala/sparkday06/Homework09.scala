package sparkday06

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}


case class Quest(Qtime:Int,Qsize:Int)
case class local(id:String,jing:Double,wei:Double)
object Homework09 {

  def timeToTimestamp(time:String)={
    val sdf = new SimpleDateFormat("yyyyMMddHHmmss")
    val date: Date = sdf.parse(time)
    date.getTime
  }
  def main(args: Array[String]): Unit = {
//    日志格式为:
//      IP 命中率(Hit/Miss) 响应时间 请求时间 请求方法 请求URL 请求协议 状态吗 响应大小 referer 用户代理


    val sparkSession: SparkSession = SparkSession.builder()
      .appName("exercise1")
      .master("local[4]")
      .getOrCreate()
    val sc: SparkContext = sparkSession.sparkContext
    import sparkSession.implicits._
    val lines: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day01\\cdn\\cdn.txt")

    //分析CDN日志统计出访问的PV、UV、IP地址
    //1.计算思路:
    //1.1. 从每行日志中筛选出IP地址
    //1.2. 去除重复的IP得到独立IP数
    val ips: Dataset[String] = lines.map(line => {
      val regx = "^(\\d{1,3}\\.){3}\\d{1,3}".r
      val maybeString: Option[String] = regx.findFirstIn(line)
      if (maybeString == None) {
        null
      } else {
        maybeString.get
      }
    })

   import org.apache.spark.sql.functions._
    val ipcont: Dataset[Row] = ips.groupBy($"value" as "ip")
      .agg(count("*") as "ipcount")
      .sort($"ipcount" desc)
    ipcont.show()

    //有时我们不但需要知道全网访问的独立IP数，更想知道每个视频访问的独立IP数
    //1.计算思路:
    //1.1筛选视频文件将每行日志拆分成 (文件名，IP地址)形式
    //1.2按文件名分组，相当于数据库的Group by 这时RDD的结构为(文件名,[IP1,IP1,IP2,…])，这时IP有重复
    //1.3将每个文件名中的IP地址去重，这时RDD的结果为(文件名,[IP1,IP2,…])，这时IP没有重复
    val rdd2: RDD[String] = sc.textFile("G:\\Spark\\sparkSQL\\day01\\cdn\\cdn.txt")
    val res1: RDD[(Option[String], Option[String])] = rdd2.map(line => {
      val regx = "^(\\d{1,3}\\.){3}\\d{1,3}".r
      var fileresx = "\\d+\\.mp4".r
      val ip: Option[String] = regx.findFirstIn(line)
      val filename: Option[String] = fileresx.findFirstIn(line)
      (filename, ip)
    }).filter(x => x._1 != None && x._2 != None)
    val fileAndId: RDD[(String, String)] = res1.map(x=>(x._1.get.toString.trim,x._2.get.toString.trim))
    val dataFrame: DataFrame = fileAndId.toDF("fileName","ip")
    dataFrame.registerTempTable("fileAndIP_table")
    val res2: DataFrame = sparkSession.sql("select fileName,count(distinct ip) as ipcnt from fileAndIP_table group by fileName order by ipcnt desc")
    res2.show()

    //有时我想知道网站每小时视频的观看流量，看看用户都喜欢在什么时间段过来看视频
    //1.计算思路:
    //1.1.将日志中的访问时间及请求大小两个数据提取出来形成 RDD (访问时间,访问大小)，这里要去除404之类的非法请案例代码
    //1.2.按访问时间分组形成 RDD （访问时间，[大小1，大小2，….]）
    //1.3.将访问时间对应的大小相加形成 (访问时间，总大小)
    val qut: Dataset[Quest] = lines.map(line => {
      val timeregx = ".*(2017):([0-9]{2}):[0-9]{2}:[0-9]{2}.*".r
      val Qtime: String = timeregx.findFirstIn(line).getOrElse(null)
      val httpSizePattern = "\\s(200|206|304)\\s([0-9]+)\\s".r
      val httpSize: String = httpSizePattern.findFirstIn(line).getOrElse(null)

      (Qtime, httpSize)
    }).filter(_._2!=null).filter(_._1!=null).map(x=>{
      Quest(x._1.trim.split(":")(1).toInt,x._2.trim.split(" ")(1).toInt)
    })
    val res3: Dataset[Row] = qut.groupBy($"Qtime").agg(sum($"Qsize") as "sumSize")
      .sort($"Qtime" desc)
    res3.show()



    //通过用户经常连接的基站信息，判断用户的家庭地址和工作地址。

    val rdd3: RDD[String] = sc.textFile("G:\\Spark\\sparkSQL\\day01\\stay_time.txt")

    val loc: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day01\\loc.txt")
    val logrdd: RDD[(String, String, Long)] = rdd3.map(line => {
      val strs: Array[String] = line.split(",")
      val phone = strs(0)
      val id = strs(2)
      val time = timeToTimestamp(strs(1))
      if (strs(3).toInt == 1) {
        (phone, id, -time)
      } else {
        (phone, id, time)
      }
    })

    val localDS: Dataset[local] = loc.map(line => {
      val arr: Array[String] = line.split(",")
      local(arr(0), arr(1).toDouble, arr(2).toDouble)
    })

    import sparkSession.implicits._
    val dataFrame2: DataFrame = logrdd.toDF("phone","id","time")
    dataFrame2.registerTempTable("stay_time")
    val frame2: DataFrame = sparkSession.sql("SELECT phone,id,sum(time) as st_time from stay_time group by phone,id")

    val filted: Dataset[Row] = frame2.where("st_time>0")
    val res4: DataFrame = filted.join(localDS,"id")
    res4.show()
    sparkSession.stop()

  }

}
