package Exam

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.DateTime

object Test1 {
  def getHour(timelong: String): String = {
    val datetime = new DateTime(timelong.toLong)
    datetime.getHourOfDay.toString
  }
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test1").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)

    val src: RDD[String] = sc.textFile("G:\\Spark\\Spark阶段考试题\\Advert.txt")
//1.1.统计每一个省份点击TOP3的广告ID【5分】
    val maped: RDD[((String, String), Int)] = src.map(line => {
      val arr: Array[String] = line.split(" ")
      ((arr(1),arr(4)), 1)
    })
    val reduced: RDD[((String, String), Int)] = maped.reduceByKey(_+_)

    val maped2: RDD[(String, (String, Int))] = reduced.map(x => {
      (x._1._1, (x._1._2, x._2))
    })
    val res1: RDD[(String, List[(String, Int)])] = maped2.groupByKey().mapValues(_.toList.sortBy(_._2).reverse.take(3))

    println(res1.collect().toBuffer)

    //1.2.统计每一个省份每一个小时的TOP3广告ID【5分】
    val maped3: RDD[((String, String), Int)] = src.map(line => {
      val arr: Array[String] = line.split(" ")
      ((arr(1) + " " + getHour(arr(0)), arr(4)), 1)
    })
    val reduced2: RDD[((String, String), Int)] = maped3.reduceByKey(_+_)

    val maped4: RDD[((String, String), (String, Int))] = reduced2.map(x => {
      val arr: Array[String] = x._1._1.split(" ")
      ((arr(0), arr(1)), (x._1._2, x._2))
    })
    val res2: RDD[((String, String), List[(String, Int)])] = maped4.groupByKey().mapValues(_.toList.sortBy(_._2).reverse.take(3))

    println(res2.collect().toBuffer)
  }

}
