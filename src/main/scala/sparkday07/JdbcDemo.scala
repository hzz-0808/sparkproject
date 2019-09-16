package sparkday07

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

object JdbcDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("jdbc")
      .master("local[2]")
      .getOrCreate()

    //读取数据库中某个表的
    val url="jdbc:mysql://localhost:3306/db"
    var table = "user"
    val properties = new Properties()
    properties.put("driver","com.mysql.Driver")
    properties.setProperty("user","root")
    properties.setProperty("password","123456")
    val frame: DataFrame = sparkSession.read.jdbc(url,table,properties)

//    val df: DataFrame = sparkSession.read.format("jdbc").options(
//      Map("url" -> "jdbc:mysql://localhost:3306/db"
//        , "driver" -> "com.mysql.Driver"
//        , "user" -> "root"
//        , "password" -> "123456")
//    ).load()
    //写数据库
    frame.write.jdbc(url,table,properties)

    sparkSession.stop()

  }

}
