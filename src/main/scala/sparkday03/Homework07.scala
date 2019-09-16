package sparkday03

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}



object Homework07 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("homework").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)

    /**
      * 1, 数据格式: timestamp province city userid adid
      * 时间点 省份 城市 用户 广告
      * 用户ID范围:0-99
      * 省份,城市,ID相同:0-9
      * adid:0-19
      * 需求:
      * 1).统计每一个省份点击TOP3的广告ID
      * 2).统计每一个省份每一个小时的TOP3广告ID
      */
    val rdd1: RDD[String] = sc.textFile("G:\\Spark\\spark\\day03\\data\\ad.txt")

    val maped: RDD[(String, Int)] = rdd1.map((line: String) => {
      val strings: Array[String] = line.split(" ")
      (strings(1) + " " + strings(4), 1)
    })
    val reduced: RDD[(String, Int)] = maped.reduceByKey(_+_)

    val maped2: RDD[(String, (String, Int))] = reduced.map((x: (String, Int)) => {
      val strings: Array[String] = x._1.split(" ")
      (strings(0), (strings(1), x._2))
    })
    val grouped: RDD[(String, Iterable[(String, Int)])] = maped2.groupByKey()

    val provincetop3: RDD[(String, List[(String, Int)])] = grouped.map((x:(String, Iterable[(String, Int)]))=>(x._1,x._2.toList.sortBy(_._2).reverse.take(3)))

    provincetop3.foreach((x:(String,List[(String, Int)]))=>println(x))

    println("---------------------------------------------------------------------------------------")

    val sdf = new SimpleDateFormat("yyyy-MM-dd HH")
    val formatRDD: RDD[((String, String), Int)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      val formatHour: String = sdf.format(new Date(strs(0).toLong))

      ((formatHour, strs(1) + " " + strs(4)), 1)
    })
    val reduceRDD: RDD[((String, String), Int)] = formatRDD.reduceByKey(_+_)

    val maped2RDD: RDD[((String, String), (String, Int))] = reduceRDD.map((x: ((String, String), Int)) => {
      val privince: String = x._1._2.split(" ")(0)
      val ad: String = x._1._2.split(" ")(1)

      ((x._1._1, privince), (ad, x._2))

    })
    val grouped2RDD: RDD[((String, String), Iterable[(String, Int)])] = maped2RDD.groupByKey()

    val res: RDD[((String, String), List[(String, Int)])] = grouped2RDD.mapValues((x:Iterable[(String, Int)])=>x.toList.sortBy(_._2).take(3))

    println(res.collect().toBuffer)

    println("-------------------------------------------------------------------------------------")
    /**
      * 2, 基站停留时间TopN
    * 根据用户产生日志的信息,在那个基站停留时间最长
    * 19735E1C66.log 这个文件中存储着日志信息
      * 文件组成:手机号,时间戳,基站ID 连接状态(1连接 0断开)
    * lac_info.txt 这个文件中存储基站信息
      * 文件组成 基站ID, 经,纬度
    * 在一定时间范围内,求所用户经过的所有基站所停留时间最长的Top2
    * 思路:
      * 1).获取用户产生的日志信息并切分
    * 2).用户在基站停留的总时长
    * 3).获取基站的基础信息
    * 4).把经纬度的信息join到用户数据中
    * 5).求出用户在某些基站停留的时间top2
    */

    val rddlog: RDD[String] = sc.textFile("G:\\Spark\\spark\\day03\\data\\lacduration\\log\\19735E1C66.log")

    val rddlac: RDD[String] = sc.textFile("G:\\Spark\\spark\\day03\\data\\lacduration\\lac_info.txt")

    val sdf1 = new SimpleDateFormat("yyyyMMddHHmmss")
    val mappedTimeRDD: RDD[((String, String), Long)] = rddlog.map((line: String) => {
      val info: Array[String] = line.split(",")
      val date: Date = sdf1.parse(info(1))
      var timestamp: Long = date.getTime
      timestamp= if(info(3) == "1") -timestamp else timestamp
      ((info(0), info(2)), timestamp)
    })

    val lac: RDD[(String, (String, String))] = rddlac.map((x: String) => {
      val strs: Array[String] = x.split(",")
      (strs(0), (strs(1), strs(2)))
    })
    val groupedTimeRDD: RDD[((String, String), Iterable[Long])] = mappedTimeRDD.groupByKey()

    val addRDD: RDD[((String, String), Long)] = groupedTimeRDD.mapValues(_.reduce((x:Long,y:Long)=>x+y))

    val log: RDD[(String, (String, Long))] = addRDD.map((x:((String, String), Long))=>(x._1._2,(x._1._1,x._2)))

    val resrdd1: RDD[(String, ((String, Long), (String, String)))] = log.join(lac)

    val resrdd2: RDD[(String, (String, String, String, Long))] = resrdd1.map((x: (String, ((String, Long), (String, String)))) => {
      (x._2._1._1, (x._1, x._2._2._1, x._2._2._2, x._2._1._2))
    })
    val groupedbyphone: RDD[(String, Iterable[(String, String, String, Long)])] = resrdd2.groupByKey()

    val sorted: RDD[(String, List[(String, String, String, Long)])] = groupedbyphone.mapValues(_.toList.sortBy((x:(String, String, String, Long))=>x._4).reverse.take(2))

    val res5: RDD[(String, List[(String, String, String)])] = sorted.mapValues(_.map((x:(String, String, String, Long))=>(x._1,x._2,x._3)))

    println(res5.collect().toBuffer)

    //println(res2.collect().toBuffer)

    /**
      * 3, 数据格式如下：
      * 下面每一条数据代表用户发送的一条tweets(类似微博)数据
      * 统计内容：
      * 1）统计tweets 总条数；
      * 2）统计每个用户的tweets 条数；
      * 3）统计tweets中被@的分别是那些人；
      * 4）统计出被@次数最多的10个人；
      */
    println("--------------------------------------------------------------------------------------")

    val rddweibo: RDD[String] = sc.textFile("G:\\Spark\\spark\\day03\\data\\reduced-tweets.json")
    println("总条数："+rddweibo.collect().length)

    val userrdd: RDD[(String, String)] = rddweibo.map((x: String) => {
      val jsonObj: JSONObject = JSON.parseObject(x)
      val value: AnyRef = jsonObj.get("user")
      val user: String = value.asInstanceOf[String]
      (user, x)
    })
    val groupedbyuser: RDD[(String, Iterable[String])] = userrdd.groupByKey()

    val resuser: RDD[(String, Int)] = groupedbyuser.mapValues(_.toArray.length)

    println(resuser.collect().toBuffer)

    println("--------------------------------------------------")
    val beAtuples: RDD[String] = rddweibo.flatMap((x: String) => {
      val jsonObj: JSONObject = JSON.parseObject(x)
      val text: String = jsonObj.get("text").asInstanceOf[String]
      val reg = "@(\\S+)".r
      val beAt: List[String] = reg.findAllIn(text).toList

      val at: List[String] = beAt.map((x: String) => {
        val newx = x.replace("@", "")
        newx
      })
      at
    })
    println(beAtuples.collect().toBuffer)
    println("--------------------------------------------------")
    val res10: RDD[(String, Int)] = beAtuples.map((_,1))
    val res11: RDD[(String, Int)] = res10.reduceByKey(_+_)
     val res12: Array[(String, Int)] = res11.sortBy(_._2).collect().reverse.take(10)

    println(res12.toBuffer)


    sc.stop()
  }

}
