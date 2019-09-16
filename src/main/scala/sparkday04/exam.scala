package sparkday04

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Exam {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("homework").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)



    val rdd1: RDD[String] = sc.textFile("G:\\Spark\\spark\\test.txt")
    //（1）请统计出一共有多少个不同的ID；
    val ids: RDD[String] = rdd1.flatMap(_.split(" "))
    val idcnt: Long = ids.distinct().count()
    println(s"一共有$idcnt"+"个不同的ID")

    //（2）统计出一共有多少个不同的(ID,ID)对；
    val idpairs: RDD[(String, String)] = rdd1.map(line => {
      val idpair: Array[String] = line.split(" ")
      (idpair(0), idpair(1))
    })
    val idpaircnt: Long = idpairs.distinct().count()
    println(s"一共有$idpaircnt"+"个不同的(ID,ID)对")


    //（3）统计出每个用户关注的用户数量。
    val groupedbyid: RDD[(String, Iterable[String])] = idpairs.distinct().groupByKey()
    val res1: RDD[(String, Int)] = groupedbyid.mapValues(_.toList.length)
    val allid: RDD[(String, Int)] = ids.distinct().map((_,0))

    val res3: RDD[(String, Int)] = allid.leftOuterJoin(res1).map(x => {
      if (x._2._2 == None)
        (x._1, 0)
      else
        (x._1, x._2._2.get)
    })
    res3
    println(res3.collect().toBuffer)

    //（4）统计粉丝最多用户以及粉丝数
    val beliked: RDD[(String, String)] = rdd1.map(line => {
      val id: Array[String] = line.split(" ")
      (id(1), id(0))
    })
    val res4: RDD[(String, Iterable[String])] = beliked.distinct().groupByKey()
    val res5: Array[(String, Int)] = res4.mapValues(_.toList.length).sortBy(_._2,false).take(1)
    println(res5.toBuffer)

  }
}
