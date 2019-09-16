package scaladay02

import scala.collection.mutable

object MapDemo {

  def main(args: Array[String]): Unit = {
    //键值对构建map
    val map1=Map("hhh"->20,"dash"->9,"hzz"->18)
    println(map1.getClass)

    //元组形式初始化map
    val map2=mutable.Map(("hhh",20),("hsdf"->9),("asjdh"->63))
    println(map2.getClass)

    //访问集合元素
    println(map1.get("hhh"))
    println(map1.get("hhh").get) //查找的键不存在会出异常
    println(map1.get("dhdh")) //查找的键不存在返回None

    println(map1("hhh"))
    //println(map1("dhdh"))  //查找的键不存在会出异常

    println(map1.getOrElse("hhh",0))
    println(map1.getOrElse("hjds",100))

    //修改map,如果键存在则修改值，不存在则插入
    map2("hhh")=34
    map2("kkk")=24


    //如果键存在则修改值，不存在则插入
    map2.update("hhh",70)
    map2.update("hhh2",70)

    for((k,v) <- map2){
      println(k+":"+v)
    }

    //添加元素
    map2 +=("hhh3"->89)
    map2 +=(("hhh4",90))
    map2 +=(("hhh7",90),("hhh5",100))

    //添加一个集合,存在相同的元素则覆盖
    map2 ++=map1

    println("--------------------------------------------------")
    for((k,v) <- map2){
      println(k+":"+v)
    }
    //删除，给定键，删除键对应的键值对
    val res = map2 - "hhh7"
    val res2 = map2 -- Array("hhh5","hhh3")

    println("---------------------res-----------------------------")
    for((k,v) <- res){
      println(k+":"+v)
    }

    println("----------------------res2----------------------------")
    for((k,v) <- res2){
      println(k+":"+v)
    }

    map2 -= "hhh2" //在原集合上直接进行减操作
    //map2 = map2 - "hhh2" //在原集合上进行键操作之后赋值给一个另一个集合（map2必须是由var定义的）

    //遍历
    println("----------------------res2----------------------------")
    for((k,v) <- res2)
      println(k+":"+v)
    println("----------------------键值对遍历----------------------------")
    for(item<-res2){
      println(item)
    }
    println("----------------------keyset遍历----------------------------")
    val keyset = map2.keySet
    for(key <- keyset){
      println(key+"-->"+map2.get(key).get)
    }
    println("----------------------迭代器遍历----------------------------")
    val values = map2.values
    for(value<-values){
      println(value)
    }
  }
}
