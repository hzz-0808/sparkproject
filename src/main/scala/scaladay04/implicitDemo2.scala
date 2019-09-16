package scaladay04

class Inta(val x:Int){
  def toLonger={
    println("toLonger is called")
    (x+1).toLong
  }
}

object Inta{
  implicit def fun(x:Int)={
    new Inta(x)
  }
}


object implicitDemo2 {

  def main(args: Array[String]): Unit = {
    import Inta._
    println(3.toLonger)


    var flag = false
    for(i <-1 to 10 if(!flag)){
      if(i == 5){
        println(i)

      }
      val flag = true
    }

    val arr = Array(1,2,4,6)
    println(arr.foldLeft(10)(_+_))

    val list = List(1,2)
    val res2 = list match {
      case 0 :: Nil => "0"  //列表里只有一个0
      case x :: y :: Nil => x + " " + y //列表里有两个元素
      case 0 :: tail => "0 ..."   //列表以0开头
      case _ => "something else"
    }
    println(res2)

  }
}
