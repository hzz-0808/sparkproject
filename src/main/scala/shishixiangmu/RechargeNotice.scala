package shishixiangmu

import com.alibaba.fastjson.{JSON, JSONObject, JSONPObject}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
  * 1.业务概况---必做（结果存入Redis）
  * 1)统计全网的充值订单量, 充值金额, 充值成功数
  * 2)实时充值业务办理趋势, 主要统计全网的订单量数据
  */
case class logg(orderId:String,serviceName:String,chargefee:String
                ,bussinessRst:String,provinceCode:String,logOutTime:String)
case class province_map(provinceCode:String,province:String)
object RechargeNotice {
  def getConnection()={
    val config = new JedisPoolConfig()
    //最大连接数
    config.setMaxTotal(30)
    //最大空闲连接数
    config.setMaxIdle(10)
    //是否进行有效检查
    config.setTestOnBorrow(true)
    //config,主机，端口号，连接超时时长
    val pool = new JedisPool(config,"192.168.159.149",6379,1000)

    val resource: Jedis = pool.getResource

    resource
  }
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("RechargeNotice")
      .master("local[2]")
      .getOrCreate()
    import sparkSession.implicits._
    import  org.apache.spark.sql.functions._
    val src: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkStreaming\\day05\\充值平台实时统计分析\\cmcc.json")


    //统计全网的充值订单量,充值金额,充值成功数
    val src_table: Dataset[logg] = src.map(line => {
      val obj: JSONObject = JSON.parseObject(line)
      val orderId: String = obj.getString("orderId")
      val serviceName: String = obj.getString("serviceName")
      val chargefee: String = obj.getString("chargefee")
      val bussinessRst: String = obj.getString("bussinessRst")
      val provinceCode: String = obj.getString("provinceCode")
      val logOutTime: String = obj.getString("logOutTime")

      logg(orderId, serviceName, chargefee, bussinessRst,provinceCode,logOutTime)
    })
    val reChargeNotifyReq_df: DataFrame = src_table.toDF().where("serviceName='reChargeNotifyReq'")

    val makeNewOrder: DataFrame = src_table.toDF().where("serviceName='makeNewOrder'")

    val res1: DataFrame = makeNewOrder.join(reChargeNotifyReq_df,"orderId")
      .agg(countDistinct("orderId")) as "CountAllId"
    val res2: DataFrame =  makeNewOrder.join(reChargeNotifyReq_df,"orderId")
      .agg(sum(col("chargefee").cast("Int"))).as("ChargeSum")
    val res3: DataFrame =  makeNewOrder.join(reChargeNotifyReq_df,"orderId")
      .filter("bussinessRst='0000'")
      .agg(count("bussinessRst")).as("SeccessCharge")

    //2)实时充值业务办理趋势, 主要统计全网的订单量数据
    val res4: DataFrame = makeNewOrder.agg(countDistinct("orderId")) as "CountAllId"
//

    res2.show()
    res3.show()
    res4.show()
    //存进Redis
    val jedis: Jedis = getConnection()
    jedis.set("全网的充值订单量",res1.collect()(0).toString())
    jedis.set("全网的充值金额",res2.collect()(0).toString())
    jedis.set("全网的充值成功数",res3.collect()(0).toString())
    jedis.set("全网的订单量",res4.collect()(0).toString())

    jedis.close()

    println("-----------------------------------------------------------------------")
    //全国各省充值业务失败量分布
    val pro_map: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\sparkStreaming\\day05\\充值平台实时统计分析\\provinceMap.txt")
    val prov_map: Dataset[province_map] = pro_map.map(line => {
      val arr: Array[String] = line.split(" ")
      province_map(arr(0), arr(1))
    })
    val prov_map_df: DataFrame = prov_map.toDF()

    val failed_prov_count: DataFrame = prov_map_df.join(reChargeNotifyReq_df,"provinceCode")
      .join(makeNewOrder, "orderId")
      .where("bussinessRst<>'0000'")
      .groupBy("province").agg(count("orderId"))
    val failed_pro_hour_count: DataFrame = prov_map_df.join(reChargeNotifyReq_df, "provinceCode")
      .join(makeNewOrder, "orderId")
      .where("bussinessRst<>'0000'")
      .groupBy("province", "substring(8,2)").agg(count("orderId"))

    //以省份为维度统计订单量排名前10的省份数据,并且统计每个省份的订单成功率，
    //只保留一位小数，存入MySQL中，进行前台页面显示。
    val res6: Array[Row] = makeNewOrder.join(reChargeNotifyReq_df, "orderId")
      .groupBy("provinceCode")
      .agg(count("orderId") as "IdCount",
        countDistinct("bussinessRst='0000'") as "SuccessCount",
        col("SuccessCount").cast("Double") / col("IdCount") as "SuccessRate")
      .orderBy($"Idcount" desc)
      .join(prov_map_df,"provinceCode")
      .select("province",  "IdCount","SuccessRate")
      .take(10)



    sparkSession.stop()
  }

}
