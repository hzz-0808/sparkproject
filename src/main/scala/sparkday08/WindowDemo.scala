package sparkday08

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowDemo {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("streamingwc2").setMaster("local[2]")
    //创建入口点
    val ssc = new StreamingContext(sparkConf,Seconds(10))
    //设置检查点目录
    ssc.checkpoint("D://windowck")
    //获取实时数据流
    val srcDs: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.159.149",8989)

    val wordTup: DStream[(String, Int)] = srcDs.flatMap(_.split(" ")).map((_,1))

    //transform foreachrdd
    //把对实时流的数据抽象Dstream的运算转换成对DStream中的
    // 每一个RDD进行操作，使用RDD的算子
    //val wordTup2: DStream[(String, Int)] = srcDs.transform((rdd:RDD[String])=>rdd.flatMap(_.split(" ")).map((_,1)))

    //srcDs.foreachRDD((rdd:RDD[String])=>rdd.foreach(println))

    val res: DStream[(String, Int)] = wordTup.reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Seconds(30),Seconds(10))

    res.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
