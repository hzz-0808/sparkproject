package scaladay03

import scala.collection.mutable

//密封类
//类和其子类在一个源文件中定义
sealed abstract class sMsg{

}

case class pMsg(title:String) extends sMsg
case class eMsg(title:String) extends sMsg

object SealedClassDemo {

  def matchMethod(obj:sMsg): Unit ={
    obj match {
      case pMsg(t) => println(t)
      case eMsg(t) => println(t)
    }
  }


  def main(args: Array[String]): Unit = {
    matchMethod(eMsg("email info"))

    val (a,b,c) = (10,20,30)

    val a1::b2::c2 = List(10,20,30,50)

    println(a1)
    println(b2)
    println(c2)

    val map = mutable.HashMap

  }
}
