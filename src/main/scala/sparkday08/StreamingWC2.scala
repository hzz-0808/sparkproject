//package sparkday08
//
//import org.apache.spark.{HashPartitioner, SparkConf}
//import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//object StreamingWC2 {
//  def main(args: Array[String]): Unit = {
//    val sparkConf = new SparkConf()
//      .setAppName("streamingwc2").setMaster("local[2]")
//    //创建入口点
//    val ssc = new StreamingContext(sparkConf,Seconds(10))
//    //设置一个检查点目录，记录历史数据
//    ssc.checkpoint("D://sparkstreaming")
//    //获取一道实时数据流，构建DStream
//    val ds: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.159.149",8989)
//
//    val wds: DStream[(String, Int)] = ds.flatMap(line=>line.split(" ")).map((_,1))
//
////    val res: DStream[(String, Int)] = wds.updateStateByKey(updateFun
////      ,new HashPartitioner(ssc.sparkContext.defaultParallelism)
////      ,true)
////
////    res.print()
////    ssc.start()
////    ssc.awaitTermination()
//    val mappingFun = (key:String,count:Option[Int],state:State[Int])=>{
//
//  count.getOrElse(0)+state.getOption().getOrElse(0)
//}
//    StateSpec.function(mappingFun)
//  }
//
//  //第一个参数代表当前批次数据出现的key，
//  // Seq中是key对应的value集合，
//  // 第三个当前key对应的代表历史数据
////  val updateFun=(it:Iterator[(String,Seq[Int],Option[Int])])=>{
////    it.map(item=>{
////      (item._1,item._2.sum+item._3.getOrElse(0))
////    })
////  }
//
//}
