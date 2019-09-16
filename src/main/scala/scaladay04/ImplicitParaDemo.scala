package scaladay04



object ImplicitParaDemo {

  trait Adder[A]{
    def add(x:A,y:A):A
  }

  implicit val adderObj = new Adder[Int]{
    override def add(x: Int, y: Int): Int = x+y+100
  }

  def add(x:Int,y:Int)(implicit adder: Adder[Int])={
    adder.add(x,y)
  }

  def main(args: Array[String]): Unit = {
    println(add(10,100))
  }

}
