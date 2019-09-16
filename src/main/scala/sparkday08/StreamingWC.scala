package sparkday08

import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.expressions.Second
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWC {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("streamingwc").setMaster("local[2]")
    //创建入口点
    val ssc = new StreamingContext(sparkConf,Seconds(10))

    val rids: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.159.149",8989)

    val wc: DStream[(String, Int)] = rids.flatMap(_.split(" ")).map((_,1))

    val res: DStream[(String, Int)] = wc.reduceByKey(_+_)
    res.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
