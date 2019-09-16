package sparkday08

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Student(sub:String,name:String)
object WinFunDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("windowdemo")
      .master("local[2]")
      .getOrCreate()

    import sparkSession.implicits._
//    val df: DataFrame = sparkSession.sparkContext.
//      parallelize(Seq(("tom", 90), ("jerry", 100), ("marry", 80)))
//      .toDF("name", "age")
    val src: RDD[String] = sparkSession.sparkContext.textFile("G:\\Spark\\sparkSQL\\day02\\aa.txt")

    val rdd: RDD[Student] = src.map(line => {
      val arr: Array[String] = line.split(" ")
      Student(arr(0), arr(1))
    })

    val df: DataFrame = rdd.toDF
    df.createOrReplaceTempView("student_table")
    //统计每个科目每个人的票数
    val df1: DataFrame = sparkSession.sql("select sub,name,count(name) counts from student_table " +
      "group by sub,name")
    df1.createOrReplaceTempView("tmp_table")

    val res: DataFrame = sparkSession.sql("select * from " +
      "(select *," +
      "row_number() over(distribute by sub order by counts desc) rn " +
      "from tmp_table)" +
      " where rn<2"
    )

    res.show()
    sparkSession.stop()
  }

}
