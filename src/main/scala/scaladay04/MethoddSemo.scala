package scaladay04

object MethoddDemo {

  def main(args: Array[String]): Unit = {
    println(fatorial(10))

    println(constructList("aa",5))

    fun("hello")
  }

  //方法的嵌套，方法里面定义新的方法，一般定义完即调用
  //计算阶乘
  def fatorial(x:Int)={
    def factor(x:Int,res:Int): Int ={
      if(x<=1){
        res
      }else
        factor(x-1,x*res)
    }

    factor(x,1)
  }

  //方法的多态：方法的参数类型参数化，相当于方法的参数可以支持不同的数据类型
  //构建一个列表，可以构建不同类型的列表
  def constructList[A](x:A,length:Int):List[A]={
    if(length<=0)
      Nil
    else
      x::constructList(x, length-1)
  }

  def fun[B](x:B)={
    if(x.isInstanceOf[String]){
      println(" \" " + s"$x"+"\" is String")
    }else{
     // println(s"\"$x\" is not String")
    }
  }
}
