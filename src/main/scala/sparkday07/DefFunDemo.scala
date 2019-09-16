package sparkday07


import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql._

//自定义函数
//UDF：map，一个输入对应一个输出
//udaf：聚合，多个输入对应一个输出，继承接口实现（dataframe，dataset）
case class loged(phone:String,time:String,id:String,var state:Int)
case class Avg(var sum:Int,var count:Int)
object DefFunDemo {
  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder()
      .appName("udf")
      .master("local[*]")
      .getOrCreate()
    import  org.apache.spark.sql.functions._
    import sparkSession.implicits._
    val ds: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkSQL\\day01\\stay_time.txt")
    val rdd: RDD[String] = sparkSession.sparkContext.textFile("G:\\Spark\\sparkSQL\\day01\\stay_time.txt")

    //自定义一个map功能
    val fun = (x:Int)=>x+1
    //注册自定义函数

    val avgstate = new myAvgFun().toColumn.name("avg_state")
    sparkSession.udf.register("changeStat",fun)
    sparkSession.udf.register("myAggFun",new myAggFun)

   // sparkSession.udf.register("myAvgFun",new myAvgFun)

//    //使用自定义函数
//    val fram1: DataFrame = fram1.selectExpr("age","changeAge(age)")
//    fram1.show()
    val ds1: Dataset[loged] = ds.map(x => {
      val arr: Array[String] = x.split(",")
      loged(arr(0), arr(1), arr(2), arr(3).toInt)
    })

    val rdd1: RDD[(String, String, String, Int)] = rdd.map(x => {
      val arr: Array[String] = x.split(",")
      (arr(0), arr(1), arr(2), arr(3).toInt)
    })

    val result: Dataset[Double] = ds1.select(avgstate)

    result.show()

    val df1: DataFrame = rdd1.toDF("phone","time","id","state")

    val frame2: DataFrame = df1.selectExpr("myAggFun(state)")

    
    val frame: DataFrame = ds1.selectExpr("state","changeStat(state)")


    frame2.show()
    sparkSession.stop()
  }

}

class myAggFun extends UserDefinedAggregateFunction{
  //定义聚合函数输入的数据类型
  override def inputSchema: StructType = {
    StructType{
      List(
        StructField("count",IntegerType,true)
      )
    }

  }

  override def bufferSchema: StructType = {
    StructType{
      List(
        StructField("sum",IntegerType,true) ,
        StructField("count",IntegerType,true)
      )
    }
  }

  override def dataType: DataType = DoubleType

  //是否以一个确定的输入，一个确定的输出
  override def deterministic: Boolean = true

  //初始化每一个分区上进行聚合计算的容器
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    //初始化sum
    buffer(0) = 0
    //初始化count
    buffer(1) = 0
  }

  //遍历分区上的每个元素，然后把元素累加到聚合结果上
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    //更新分区上聚合的sum和count
    buffer(0) = buffer.getInt(0)+input.getInt(0)

    buffer(1) = buffer.getInt(1)+1
  }

  //多个分区上聚合结果进行汇总
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getInt(0)+buffer2.getInt(0)
    buffer1(1) = buffer1.getInt(1)+buffer2.getInt(1)
  }

  //返回聚合函数的最后执行结果
  override def evaluate(buffer: Row): Double = {
    buffer.getInt(0).toDouble/buffer.getInt(1)
  }
}

class myAvgFun extends Aggregator[loged,Avg,Double]{
  //聚合结果的初始化
  override def zero: Avg = {
    Avg(0,0)
  }

  //分区内局部聚合
  override def reduce(b: Avg, a: loged): Avg = {
    b.sum += a.state
    b.count += 1
    b
  }

    //分区之间汇总
  override def merge(b1: Avg, b2: Avg): Avg = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  override def finish(reduction: Avg): Double = {
    reduction.sum.toDouble/reduction.count
  }

  override def bufferEncoder: Encoder[Avg] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}