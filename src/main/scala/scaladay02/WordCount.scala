package scaladay02

import scala.collection.mutable

object WordCount {
  def main(args: Array[String]): Unit = {
    val lines = List("hello tom hello jerry", "hello suke hello", " hello tom")

    val words = lines.map(_.split(" ")).flatten.filter(_!="")

    val map = mutable.Map[String,Int]()
    for(word <- words){
      if(map.get(word) == None){
        map(word)=1
      }else {
        map(word) = map.get(word).get + 1
      }
    }

    println(map)

    val list = List(1,2,4,3,5,7,6,8)

    val list1 = list.map((x:Int)=>{if(x%2==0) x})
    println(list1)

  }

}
