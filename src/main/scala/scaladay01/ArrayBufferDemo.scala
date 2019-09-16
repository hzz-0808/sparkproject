package scaladay01

import scala.collection.mutable

object ArrayBufferDemo {

  def main(args: Array[String]): Unit = {
    //变长数组
    val arr=mutable.ArrayBuffer[Int]()

    //追加元素(一个或多个)
    arr += 10

    arr += (19,30,20)
    println(arr.toBuffer)

    //追加集合
    val arr2 = Array(1,3,5,7,9)

    arr ++= arr2

    //通过append方法追加一个或多个元素
    arr.append(90)
    arr.append(200,400,60)

    //通过insert插入一个或多个元素
    //第一个参数表示插入的位置
    arr.insert(1,55,45,56)

    //删除元素
    //从第0个位置开始删除3个元素
    arr.remove(0,3)
    arr -= 55

    //从数组结尾删除2个元素
    arr.trimEnd(2)
    //从开头删除3个元素
    arr.trimStart(3)

    //修改
    arr(0)=33333

    //查找
    println(arr(0))
    //遍历
    arr.map((x:Int)=>x*10)
    arr.map(_*20)

    //过滤
    //返回所有大于10的元素
    arr.filter((x:Int)=>x>10)

    //定长和变长的互相转换
    arr.toArray
    arr2.toBuffer



  }
}
