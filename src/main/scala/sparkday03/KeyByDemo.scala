package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeyByDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1 = sc.parallelize(List("scala","java","c"))
    //keyBY：根据每个元素按自定义的逻辑生成一个key，返回一个key value的rdd
    val rdd2: RDD[(Int, String)] = rdd1.keyBy((x:String)=>x.length)
    println(rdd2.collect().toBuffer)

    sc.stop()

  }
}
