package scaladay02

import scala.collection.mutable

object HashMapDemo {
  def main(args: Array[String]): Unit = {
    val hashMap= new mutable.HashMap[String,Int]()

    //添加元素
    hashMap("scala")=1
    hashMap.+=(("java",2))
    hashMap.put("python",3)

    //查找
    hashMap.getOrElse("python",0)
    hashMap("scala") //找不到会抛异常

    //删除
    hashMap -= "scale"
    hashMap.remove("java")

    //修改
    hashMap("scala")=8
    hashMap.update("java",9)
  }

}
