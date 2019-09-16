package scaladay01

object MethodDemo {

  //定义方法
  def add(x:Int,y:Int):Int={x+y}
  def adda(x:Int,y:Int)=x+y

  //带参数列表的方法
  def addb(x:Int)(y:Int)=x+y

  //无参方法
  def msg = println("message")
  def msg1()=println("message1")

  val fun=(x:Int,y:Int)=>{x+y
   print(x+y)}


  val fun1=(_:Int)+(_:Int)

  val fun2:((Int,Int)=>Int)=(x,y)=>x+y

  val fun3:(Int,Int)=>Int=_+_

  val fun4=new Function2[Int,Int,Int]{
    override def apply(v1: Int, v2: Int): Int = v1+v2
  }

  //无参函数
  val fun5=()=>print("没有参数的函数") //不能省略()

  //一般所有方法能表示的函数都能表示

  def main(args: Array[String]): Unit = {
    val result=add(2,3)


    val res = addb(10)(20)
    val res1=addb(10)_
    val res2=res1(20)

    println(result)
    println(res)
    println(res2)

    msg

    print(fun(5,6))

    val arr=Array(10,20,40)

    val mapfun=(x:Int)=>x*100

    def mapMeth(x:Int) = x*100
    val arr1=arr.map(mapfun)

    val arr2=arr.map(mapMeth(_))

   val arr3=arr.map(mapMeth _) //手动把方法转化成函数加 空格_
    println(arr1.toBuffer)
    println(arr2.toBuffer)

  }
}
