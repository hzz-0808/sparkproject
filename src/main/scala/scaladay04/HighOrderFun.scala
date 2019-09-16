package scaladay04

//该节函数：函数的参数或返回值是一个函数
object HighOrderFun {

  def main(args: Array[String]): Unit = {
    //函数的参数是函数
    val arr = Array(1,3,5)
    val res0 = arr.map(_+100)
    val res1 = arr.map((x:Int)=>x+100)

    val fun2 = fun1(true,"www.1000phone.com")
    println(fun2.getClass)

    println(fun2("spark","rdd"))

    val fun4 = fun3(16,20)
    println(fun4(20,30))
  }

  //返回值是一个函数
  def fun1(ssl:Boolean,domainName:String):(String,String)=>String={

    val scheme = if(ssl) "https://" else "http://"

    (moudle:String,query:String)=>s"$scheme$domainName/$moudle?$query"
  }


  def fun3(x:Int,y:Int):(Int,Int)=>Int={
    if(x>10 && y>15)
      (z:Int,w:Int)=>z+w
    else
      (z:Int,w:Int)=>w-z
  }
}
