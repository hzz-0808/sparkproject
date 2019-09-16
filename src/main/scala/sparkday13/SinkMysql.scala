package sparkday13

import java.sql.{Connection, DriverManager, Statement}

import org.apache.spark.sql.streaming.{ProcessingTime, StreamingQuery}
import org.apache.spark.sql.{DataFrame, ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.types.StructType

class mysqlSink(url:String,user:String,password:String) extends ForeachWriter[Row]{
  //初始化，建立连接
  var statement:Statement = _
  var connection:Connection = _

  override def open(partitionId: Long, version: Long): Boolean = {

    //获取连接对象
    //Class.forName("com.mysql.jdbc.Driver")
    connection = DriverManager.getConnection(url,user,password)

    statement = connection.createStatement()
    true
  }

  //实现数据写入逻辑
  override def process(value: Row): Unit = {

    val sql = "insert into t_sink values ('"+value(0).toString+"',"+value(1).toString+",'"+value(2).toString+"')"
    statement.executeUpdate(sql)
  }

  //进行资源的释放
  override def close(errorOrNull: Throwable): Unit = {
    if(statement!=null){
      statement.close()
      statement=null
    }
    if(connection!=null){
      connection.close()
      connection = null
    }

  }
}



object SinkMysql {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("input")
      .master("local[2]")
      .getOrCreate()
    //创建实时结构数据流
    //创建数据的结构化信息

    val structType: StructType = new StructType().add("name","string")
      .add("age","integer")
      .add("gender","string")
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
    val frame: DataFrame = src.select("name","age","gender")

    //结果下沉到mysql
    val query: StreamingQuery = frame.writeStream.foreach(new mysqlSink("jdbc:mysql://192.168.159.149:3306/testdb", "root", "123456"))
      .outputMode("update")
      //触发计算的时间
      .trigger(ProcessingTime("10 seconds"))
      .start()
    query.awaitTermination()


  }

}
