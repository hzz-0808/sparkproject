package scaladay04

object CloseDemo {
  //闭包

  def add(x:Int)=100+x

  //这个函数和它依赖的函数外定义的变量形成了一个闭包
  val factor=800
  def add1(x:Int)=factor+x

  def main(args: Array[String]): Unit = {
    println(add(100))
    println(add1(100))
  }

}
