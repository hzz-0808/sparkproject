package sparkday12

import org.apache.spark.sql.streaming.{ProcessingTime, StreamingQuery}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object KafkaStructedStreaming {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("kafka")
      .master("local[2]")
      .getOrCreate()
    val df: DataFrame = sparkSession.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "192.168.159.149:9092")
      .option("subscribe", "first")
      .load()

    import sparkSession.implicits._
    val frame: DataFrame = df.selectExpr("CAST(value as string)")

    val ds: Dataset[String] = frame.as[String]

    val res: DataFrame = ds.flatMap(_.split(" ")).groupBy("value").count()

    val query: StreamingQuery = res.writeStream.format("console")
      .outputMode("complete")
//      .option("checkpointLocation", "kafka/sink")
//      .option("kafka.bootstrap.servers", "192.168.159.149:9092")
//      .option("topic", "first_1")
      .trigger(ProcessingTime("5 seconds"))
      .start()
    query.awaitTermination()
  }

}
