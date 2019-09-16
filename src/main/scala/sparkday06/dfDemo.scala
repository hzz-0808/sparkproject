package sparkday06

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


case class guanzhu(gz:String,bgz:String)
object dfDemo {
  def main(args: Array[String]): Unit = {

    //创建一个程序的sparkSession
    //spark1.6 sqlContext hiveContext
    val sparkSession =sql.SparkSession.builder()
      .appName("df1")
      .master("local[4]")
      .getOrCreate()
//    val sparkConf = new SparkConf().setAppName("df1").setMaster("local[4]")
//    val sparkSession1 = SparkSession.builder().config(sparkConf)
//
//    //支持hive
//    val sparkSession2 = SparkSession.builder().appName("df1").master("local[4]")
//      .enableHiveSupport()
//      .getOrCreate()

    //基于RDD创建
//    val sc = sparkSession.sparkContext
//    val src: RDD[String] = sc.textFile("G:\\Spark\\spark\\test.txt")
//    val rdd: RDD[Row] = src.map(line => {
//      val arr: Array[String] = line.split(" ")
//      val gz = arr(0)
//      var bgz = arr(1)
//      Row(gz, bgz)
//    })
//    //定义这个数据集的结果信息scheme
//    val structType = StructType(
//      List(
//        StructField("gz", StringType, true),
//        StructField("bgz", StringType, true)
//      )
//    )
//    val dataFrame: DataFrame = sparkSession.createDataFrame(rdd,structType)
//
//    //注册一个临时表或者视图
//    dataFrame.registerTempTable("table_guanzhu")
//    //兼容sql 2003
//    val frame: DataFrame = sparkSession.sql("SELECT gz,bgz from table_guanzhu where gz='11111111'")
//
//    frame.show()
//
//    sparkSession.stop()

    //方式2 RDD=>dataFrame
//    val rdd: RDD[(String, String)] = src.map(line => {
//      val arr: Array[String] = line.split(" ")
//      val gz = arr(0)
//      var bgz = arr(1)
//      (gz, bgz)
//    })
//    import sparkSession.implicits._
//    val dataFrame: DataFrame = rdd.toDF("gz","bgz")
//    dataFrame.registerTempTable("table_guanzhu")
//    val frame: DataFrame = sparkSession.sql("SELECT * from table_guanzhu")
//
//    frame.show()
//    sparkSession.stop()

    //方式3 RDD=>dataFrame 使用反射


  }

}
