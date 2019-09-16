package scaladay01

object Bianliang {

  def main(args: Array[String]): Unit = {
    val a = 200L
    println(a)

    val d = '\u0041'
    println(d)

    println(
      """hello
        |djasd djaks
        |jsdkas
      """.stripMargin)

    var aa = 19.9.toInt
    println(aa)

    val face:Char = '☺'
    println(face+" "+face.toInt)

    print(sub(2,1))

    val res1=sub2(7)_
    val res2=res1(3)
    println(res1)
    println(res2)


  }

  def sub(x:Int,y:Int)=x-y

  def sub2(x:Int)(y:Int)=x-y

  val f:(Int)=>String={x=>x+""}

  def foo(f:Int => String)={
    println(f)
  }


}
