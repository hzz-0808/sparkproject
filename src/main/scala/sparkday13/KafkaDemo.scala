package sparkday13

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object KafkaDemo {
  def main(args: Array[String]): Unit = {

//    //创建程序入口点
//    val sparkSession = SparkSession.builder()
//      .appName("kafka demo")
//      .master("local[2]")
//      .getOrCreate()
//    val frame: DataFrame = sparkSession.readStream
//      .format("kafka")
//      .option("kafka.bootstrap.servers", "mini01:9092,mini02:9092")
//      .option("topic", "hzz")
//      .load()
//    val ds: Dataset[String] = frame.selectExpr("CAST(value AS STRING)").as[String]
//
//    //对接受到的数据实现处理逻辑
//    val res: DataFrame = ds.flatMap(_.split(" ")).groupBy("value").count()
//
//    val query: StreamingQuery = res.writeStream.format("kafka").outputMode("complete")
//      .option("kafka.bootstrap.servers", "mini01:9092")
//      .option("topic", "hzz1")
//      .start()
//    query.awaitTermination()


  }

}
