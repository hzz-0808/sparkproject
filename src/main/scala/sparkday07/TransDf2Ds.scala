package sparkday07

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

case class person(name:String,age:Int)
object TransDf2Ds {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("jdbc")
      .master("local[2]")
      .getOrCreate()

    import sparkSession.implicits._
    val df: DataFrame = sparkSession.sparkContext.
      parallelize(Seq(("tom", 90), ("jerry", 100), ("marry", 80)))
      .toDF("name", "age")
    //dataframe变成dataset
    val ds: Dataset[person] = df.as[person]
    //dataset=>dataframe
    val df2: DataFrame = ds.toDF()
    //datas=>RDD
    val rdd: RDD[person] = ds.rdd
  }

}
