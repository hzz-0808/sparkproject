package sparkday02

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object HomeWork06 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("PVTop3").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.textFile("G:\\Spark\\spark\\day02\\作业数据\\access.txt")

    val mappedrsdd = rdd.map((x:String)=>{
      val strings = x.split("\t")
      (strings(1),1)
    })

    //获取各个模块的访问量
    val reducedrdd: RDD[(String, Int)] = mappedrsdd.reduceByKey((x:Int, y:Int)=>x+y)
    reducedrdd.foreach(println)

    //获取各个学科各个模块的访问量
   val subjectrdd =  reducedrdd.map((x:(String, Int))=>{
     val url = x._1
     val host = new URL(url).getHost
     (host,x._1,x._2)
   })
    //按照学科分组
    val groupedBySub: RDD[(String, Iterable[(String, String, Int)])] = subjectrdd.groupBy((x:(String,String,Int))=>x._1)

    val sorted: RDD[(String, List[(String, String, Int)])] = groupedBySub.map((x:(String, Iterable[(String, String, Int)]))=>(x._1,x._2.toList.sortBy(_._3).reverse))

    val res: RDD[(String, List[(String, String, Int)])] = sorted.mapValues(_.take(3))
    res.foreach((x:(String, List[(String, String, Int)]))=>{
      println(x._1+":"+x._2.toBuffer)
    })

  }

}
