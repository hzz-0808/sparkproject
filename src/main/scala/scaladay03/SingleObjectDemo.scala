package scaladay03

object SingleObject{
  val arr:String = "Single"
  def method1: Unit ={
    println("Single Object")
  }
}

object SingleObjectDemo {

  def main(args: Array[String]): Unit = {
    SingleObject.arr
    SingleObject.method1
  }
}
