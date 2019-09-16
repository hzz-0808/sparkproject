package scaladay01

import scala.collection.mutable.ArrayBuffer

/**
  * 1.使用Boolean类型的控制变量跳出循环
  * 2.编写一个for循环,计算字符串中所有字母的Unicode代码的乘积。举例来说，"Hello"中所有字符串的乘积为
  * 9415087488L
  * 3.编写一个函数product(s:String)，计算练习2中提到的乘积。
  * 4，编写一个循环，将整数数组中相邻的元素置换。如，Array(1,2,3,4,5)经过置换变为Array(2,1,4,3,5)
  */
object Homework {



  //1.使用Boolean类型的控制变量跳出循环
  def main(args: Array[String]): Unit = {
    var flag=true
    for (i <- 1 to 10 if(flag)){
      println(i)
      if(i==5){
        flag=false
      }
    }
    //2.编写一个for循环,计算字符串中所有字母的Unicode代码的乘积。举例来说，"Hello"中所有字符串的乘积为
    // 9415087488L
    val str="hello"
    var a = 1
    for(i <- str){
      a *= i.toInt
    }
    println(a)


    //3.编写一个函数product(s:String)，计算练习2中提到的乘积。
    val product=(s:String)=>{
      var a = 1
      for(i <- s){
        a *= i.toInt
      }
      println(a)
      a
    }
    println(product("hello"))


    //4，编写一个循环，将整数数组中相邻的元素置换。如，Array(1,2,3,4,5)经过置换变为Array(2,1,4,3,5)
    val arr = ArrayBuffer[Int]()
    arr.append(1,2,3,4,5,6)
    for(i <- 1 to arr.length - 1 if(i%2==1)){
      var tmp = arr(i)
      arr(i)=arr(i-1)
      arr(i-1)=tmp
    }

    println(arr)
  }
}
