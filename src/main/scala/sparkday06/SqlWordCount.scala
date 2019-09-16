package sparkday06

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SqlWordCount {
  def main(args: Array[String]): Unit = {
    //创建一个入口点
    val sparkSession: SparkSession = SparkSession.builder()
      .appName("sql wordcount")
      .master("local[4]")
      .getOrCreate()
    val sc: SparkContext = sparkSession.sparkContext

    val rdd: RDD[String] = sc.textFile("G:\\Spark\\spark\\test.txt")

    val word: RDD[String] = rdd.flatMap(_.split(" "))

    import sparkSession.implicits._
    val dataFrame: DataFrame = word.toDF("word")

    //SQL风格
//    dataFrame.registerTempTable("table_word")
//    val frame: DataFrame = sparkSession.sql("select word,count(*) from table_word group by word")
//
//    frame.show()

    //val value: Dataset[Row] = dataFrame.as("word")

    val line: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\spark\\test.txt")
    val word1: Dataset[String] = line.flatMap(_.split(" "))
    //默认的列名 value
    import org.apache.spark.sql.functions._
    val res: DataFrame = word1.groupBy($"value" as "word")
      .agg(count("*") as "count")
        .orderBy($"count" desc)
    res.show()

    sparkSession.stop()
  }

}
