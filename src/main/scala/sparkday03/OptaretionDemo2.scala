package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object OptaretionDemo2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[(String, Int)] = sc.parallelize(List(("english",90),("math",80),("english",80),("math",90), ("computer",78)))
    val inpar: RDD[String] = rdd1.mapPartitionsWithIndex((index:Int,it:Iterator[(String,Int)])=>it.map(index+":"+_))
    println(inpar.collect().toBuffer)

    //combineByKey
    //计算每个学科的平均成绩(学科,(学科总分,参与人数))
    //使用（key，value）根据key分组，对value进行聚合，聚合的结果类型和value类型不一致的时候
    //使用combineByKey,第一个函数，定义类型转换
    val rdd2 = rdd1.combineByKey((x:Int)=>{println("first:"+x+":"+1);(x,1)}
      ,(acc:(Int,Int),score:Int)=>(acc._1+score,acc._2+1)
      ,(acc1:(Int,Int),acc2:(Int,Int))=>(acc1._1+acc2._1,acc1._2+acc2._2))
    val res: RDD[(String, Int)] = rdd2.map((x:(String,(Int,Int)))=>(x._1,x._2._1/x._2._2))
    println(res.collect().toBuffer)


  }

}
