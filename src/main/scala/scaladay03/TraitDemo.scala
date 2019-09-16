package scaladay03

trait Logger{

  var length:Int //抽象属性，没有初始值

  var content:String = ""

  def printMsg(mag:String)  //抽象方法

  def printMsg2(mag:String): Unit ={
    println(mag)
  }
}

//实现接口，如果没有其他继承的类，直接使用extends
//实现抽象属性，抽象方法，可以不用overwrite关键字，但是重写具体方法一定要加
//class WriteLog extends Logger{
//  val length = 0
//
//  override def printMsg(mag: String): Unit = {
//    println(mag)
//  }
//}
//如果继承类的同时也要实现接口，需要使用with，如果实现多个接口，就是用多个with
//class WriteLog2 extends Person with Logger{
//  val length = 0
//
//  override def printMsg(mag: String): Unit = {
//    println(mag)
//  }
//}

object TraitDemo {

}
