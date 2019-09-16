package scaladay01

import util.control.Breaks._
object ForDemo {

  def main(args: Array[String]): Unit = {

//    var flag = false
//    for(i <- 1 to 10 if(!flag)){
//      if(i!=5){
//        println(i)
//      }
//      flag=true
//    }
    //使用breakable包住整个循环语句，功能就是java中的break
    //包的是循环体，功能就是java中的continue功能

      for (i <- 1 to 10) {
        breakable {
        if (i == 5) {

          break()
        }
        println(i)
      }
    }
  }
}
