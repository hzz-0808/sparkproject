package sparkday11

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaReceiverDemo {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName("kafka receiver").setMaster("local[2]")
    //创建入口点
    val ssc = new StreamingContext(sparkConf,Seconds(10))
    ssc.checkpoint("ck_0907")

    //模式匹配
    val Array(zkQuorum,group,topics,numThreads)=args

  }

}
