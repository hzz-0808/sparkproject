package sparkday07

import org.apache.spark.sql.{DataFrame, SparkSession}

object ApiDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder()
      .appName("api demo")
      .master("local[2]")
      .getOrCreate()

    val df: DataFrame = sparkSession.read.parquet("person.parquet")

    df.show()
  }

}
