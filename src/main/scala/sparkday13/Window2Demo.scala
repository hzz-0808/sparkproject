package sparkday13

import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.types.TimestampType
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import scala.collection.mutable

object Window2Demo {
  def main(args: Array[String]): Unit = {
    //创建程序入口点
    val sparkSession: SparkSession = SparkSession.builder().appName("window")
      .master("local[2]").getOrCreate()

    import org.apache.spark.sql.functions._
    import sparkSession.implicits._
    //获取一道实时数据流
    val df: DataFrame = sparkSession.readStream
      .format("socket")
      .option("host", "192.168.159.149")
      .option("port", 8999)
      .option("includeTimestamp", true)
      .load()

    //定义一个窗口
    val ds: Dataset[(String, Timestamp)] = df.as[(String,Timestamp)]
    val wdf: DataFrame = ds.flatMap((item: (String, Timestamp)) => {
      val arr: mutable.ArrayOps[String] = item._1.split(" ")
      arr.map((_, item._2, 1))
    }).toDF("word", "time", "count")

    val sorted: Dataset[Row] = wdf.groupBy(window($"time".cast(TimestampType) as("timestamp"), "30 seconds", "10 seconds") as "wind", $"word")
      .agg(sum("count") as "counts").sort("counts")

    val query: StreamingQuery = sorted.writeStream.outputMode("complete").format("console").start()
    query.awaitTermination()
  }

}
