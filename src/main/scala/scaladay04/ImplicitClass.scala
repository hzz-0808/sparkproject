package scaladay04

object OuterObject{
  implicit class A(x:Int){
    def printx(x:Int)={
      println(s"x的值是：$x")
    }
  }
}

object ImplicitClassDemo {

  def main(args: Array[String]): Unit = {

    import OuterObject._
    val x = 10
    x.printx(x)
  }
}
