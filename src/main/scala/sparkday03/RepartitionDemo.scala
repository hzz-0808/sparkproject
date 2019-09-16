package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RepartitionDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[(String, Int)] = sc.parallelize(List(("english",90),("math",80),("english",80),("math",90), ("computer",78)))

    println(rdd1.partitions.length)
    val repardd: RDD[(String, Int)] = rdd1.repartition(10)

    println(repardd.partitions.length)

    val rdd2: RDD[(String, Int)] = repardd.coalesce(5)
    println(rdd2.partitions.length)

    //coalesce
    //分区数目从大到小可以不发生shuffle，从小到大必须要发生shuffle
  }

}
