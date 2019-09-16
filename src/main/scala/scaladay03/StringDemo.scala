package scaladay03

object StringDemo {

  def main(args: Array[String]): Unit = {
    val name = "zhangsan"
    println(s"hello,$name")
    println(s"${println("hi XX");var i = 10;i+=10;println(i)}")

    //f:格式化，规则和C语言相同

    val d = 10.32942834092
    println(f"$d%6.2f")

    //raw
    println(raw"ss\nsss")
  }

}
