package scaladay02

import scala.collection.immutable.TreeMap
import scala.collection.mutable

object MappDemo {

  def main(args: Array[String]): Unit = {
    //构建一个不可变的map
    val map1 = Map("zhangsan"->18,"lisi"->19,"wangwu"->20)
    println(map1.getClass)
    println(map1)
    //使用元组方式构建不可变map
    val map2 = Map(("zhangsan",18),("lisi",9))

    //构建可变Map
    val map3 = mutable.Map("zhangsan"->17,"lisi"->15,"wangwu"->19,"zhangliu"->20)

    println("-----------------------------访问Map值-------------------------------")

    println(map3("zhangsan")) //如果key存在，返回相应的value值，否则抛出异常
    //println(map3("zhangsan1")) //java.util.NoSuchElementException
    println(map3.get("lisi")) //some,如果key值存在，返回some，可以进一步通过get得到value
                                //如果key不存在，返回None，进一步使用get时会抛出异常
    println(map3.get("lisi").get)

    println(map3.getOrElse("lisi",0)) //如果key值存在，返回相应的value，反之，返回默认值

    println("-----------------------------修改可变map信息-------------------------------")

    map3("zhangsan")=30 //改变key对应的value值，key不存在则添加键值对
    println(map3)

    map3.update("lisi",40)
    println(map3)

    map3("qianqi")=29 //添加键值对，如果key存在，则修改相应的value值
    println(map3)

    map3 += ("zhengba"->23)

    map3 += (("zhouer",23),("sunyi",21))

    map3 += ("tianshi"->24,"zhaojiu"->25)

    println(map3)

    map3 ++= map1 //添加一个map所有的键值对，遇到相同的key则覆盖原来的value值

    println(map3)
    println("-----------------------------删除map键值对-------------------------------")

    val res = map3 - "zhangsan"
    println(res)

    map3-="zhangsan"
    println(map3)

    map3 --= List("zhangliu","lisi") //删除多个键值对
    println(map3)

    map3 -= ("sunyi","zhengba")
    println(map3)
    println("-----------------------------遍历Map-------------------------------")

    val keySet = map3.keySet //返回一个key的set集合
    for(key <- keySet){
      println(key+":"+map3.get(key).get)
    }

    for(item <- map3){
      println(item)
    }

    val keys = map3.keys //返回一个key的迭代器
    println(keys)

    val values = map3.values //返回一个value的迭代器
    for(x <- values)
      println(x)

    for((k,v)<-map3){
      print(k+":"+v+"\t")
    }

    println("-----------------------------HashMap-------------------------------")

    //创建hashmap
    val hm = mutable.HashMap[String,Int]()
    //添加数据
    hm("java")=1
    hm += (("hadoop",2),("spark",7))

    hm.put("flume",4)
    println(hm)

    hm.remove("hadoop")

    hm -= "spark"
    println(hm)

    println("-----------------------------TreeMap-------------------------------")

    val tm = TreeMap(2->'a',3->'b',1->'e') //按照key进行排序
    println(tm)


  }
}
