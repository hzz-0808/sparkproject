package sparkday11

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 1，有一批用户实时搜索的数据
  * 数据格式:
  * 访问时间 用户id 查询词 该URL在返回结果中的排名 用户点击的顺序号 用户点击的URL
  * 部分数据如下：
  * 20111230000152	792e9ba13625623176d		网盘	 3	1	http://u.115.com/
  * 20111230000010	285f88780dd0659f5fc8acc7	IQ数码	 1 	1	http://www.iqshuma.com/
  * 20111230000005	57375476989eea12893c0	奇艺高清 1	1	http://www.qiyi.com/
  * 请实时统计出当天热搜词排行榜前10名（20）。
  */
object exam {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("exam").setMaster("local[2]")
    //创建入口点
    val ssc = new StreamingContext(sparkConf,Seconds(10))
    ssc.checkpoint("D://exam")

    val src: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.159.149",8989)

    val mapeds: DStream[(String, Int)] = src.map(line => {
      val arr: Array[String] = line.split(" ")
      (arr(2), 1)
    })

    val res: DStream[(String, Int)] = mapeds.updateStateByKey(updateFun
      ,new HashPartitioner(ssc.sparkContext.defaultParallelism)
      ,true)

    val res2: DStream[(String, Int)] = res.transform((rdd:RDD[(String,Int)])=>rdd.sortBy(_._2,false))

    res2.foreachRDD((rdd:RDD[(String,Int)])=>{
      val tuples: Array[(String, Int)] = rdd.take(10)
      tuples.foreach(x=>{
        println(x._1)
      })

    })
    ssc.start()
    ssc.awaitTermination()

  }

  //第一个参数代表当前批次数据出现的key，
  // Seq中是key对应的value集合，
  // 第三个当前key对应的代表历史数据
  val updateFun=(it:Iterator[(String,Seq[Int],Option[Int])])=>{
    it.map(item=>{
      (item._1,item._2.sum+item._3.getOrElse(0))
    })
  }

}
