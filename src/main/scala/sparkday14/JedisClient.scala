package sparkday14

import java.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object JedisClient {

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
    val jedis: Jedis = getConnection()

//    jedis.set("jskey","strvalue_jedis")
//
//    val str: String = jedis.get("jskey")
//    println(str)
//
//    jedis.del("jskey")
//    jedis.mset("hahaha","红红火火恍惚","hehehe","呵呵")
//    val list: util.List[String] = jedis.mget("hahaha","hehehe")
//
//    println(list)

    //list类型
    jedis.lpush("listkey","ddas","hsadfj","hdaujs","dh")

    jedis.rpush("listkey","hello","java")

    val str: String = jedis.lpop("listkey")

//    jedis.append("listkey","chinese")

    val list1: util.List[String] = jedis.lrange("listkey",0,-1)

    println(list1)
    //资源释放
    jedis.close()
  }

}
