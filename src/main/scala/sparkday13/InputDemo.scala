package sparkday13

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

object InputDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("input")
      .master("local[2]")
      .getOrCreate()
    //创建实时结构数据流
    //创建数据的结构化信息

    val structType: StructType = new StructType().add("name","string").add("age","integer")
    val src: DataFrame = sparkSession.readStream
      .schema(structType)
      //每次触发计算处理的最大文件数
      .option("maxFilesPerTigger", 10)
      //是否优先处理新加入的文件
      .option("latestFirst", true)
      //文件名标识，true标识文件名唯一的标识每个文件（即使文件来自不同目录）
      .option("fileNameOnly", true)
      //文件路径
      .json("person.json")

    //写对数据的处理逻辑
    val frame: DataFrame = src.select("name")
    //结果下沉
    val query: StreamingQuery = frame.writeStream.outputMode("append")
      .format("console")
      .start()
    query.awaitTermination()
  }

}
