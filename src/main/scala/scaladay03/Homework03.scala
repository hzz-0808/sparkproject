package scaladay03

import sun.security.util.Length

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

//  1、编写Person类，主构造器接受一个字符串，字符串形式为名字，空格，姓，如new Person(“Fred Smith”)。
//  提供只读属性firstName和lastName。
class Person1(name:String){

    val strings = name.split(" ")
    val firstName = strings(0)
    val lastName = strings(1)
}
//  2、定义一个Point类，使得我们不用new就可以直接使用Point(3,4)来构建类的实例。
class Point(val x:Double,val y:Double){

}

object Point{
  def apply(x: Double, y: Double): Point = new Point(x,y)
}

//  3、定义一个抽象类Shape，一个抽象方法centerPoint, 以及该抽象类的子类Rectangle和Circle，为子类提供合
//  适的构造器，并重写centerPoint方法。

abstract class Shape(var x:Double,var y:Double){

  def centerPoint
}

class Circle(x:Double,y:Double) extends Shape(x:Double,y:Double){

  var r:Double = _
  def this(x: Double,y: Double,r:Double){
    this(x,y)
    this.r=r

  }

  override def centerPoint: Unit = {
    println("圆心：("+x+","+y+")；半径:"+r+"")
  }
}

class Rectangle(x:Double,y:Double) extends Shape(x:Double,y:Double){

  var length:Double = _
  var height:Double = _

  def this(x: Double,y: Double,length:Double,height:Double){
    this(x,y)
    this.length=length
    this.height=height

  }

  override def centerPoint: Unit = {
    println("中心：("+x+","+y+")；长："+length+"；宽："+height)
  }
}


object Homework03 {

  def main(args: Array[String]): Unit = {

    val p = new Person1("Fred Smith")
    println(p.firstName)
    println(p.lastName)

    val point = Point(3,4)
    println(point.x+","+point.y)

    val circle = new Circle(1,3,1)
    circle.centerPoint
    val rectangle = new Rectangle(1,3,4,5)
    rectangle.centerPoint

    //  4、利用模式匹配，对List做出如下匹配条件 （1）不管元素个数，List是否以1开头
    //  （2）List含有三个元素，并且第二个元素是2 （3）List含有两个元素，并且最后一个元素是3 （4）都不符合以上
    //  条件

    val list = List(1,4,5)

    list match {
      case List(1,_*) => println("List以1开头")
      case List(_,2,_) => println("List含有三个元素，并且第二个元素是2")
      case List(_,3) =>println("List含有两个元素，并且最后一个元素是3")
      case _ => println("都不符合以上条件")
    }

    //  5、给定一个List （1）选出List中所有的非零偶数 （2）选出List中所有大于2的数 （3）将List中的所有值都乘以2

    val list2 = List(1,3,5,6,8,7,2,0)
    val list3 = list2.filter((x:Int)=>{x%2==0 && x!=0})
    val list4 = list2.filter(_>2)
    val list5 = list2.map(_*2)
    println(list3)
    println(list4)
    println(list5)


    // 6、编写函数values(fun:(Int)=>Int,low:Int,high:Int),该函数输出一个集合，对应给定区间内给定函数的输入和输
    //  出。比如，values(x=>x*x,-5,5)应该产出一个对偶的集合(-5,25),(-4,16),(-3,9),…,(5,25) 。**
    val values=(fun:(Int)=>Int,low:Int,high:Int)=>{
      //var map = mutable.HashMap[Int,Int]()

      val list = ListBuffer[(Int,Int)]()
      for(i<- low to high){
        //map.put(i,fun(i))
        list.append((i,fun(i)))
      }
      list
    }
    println(values(x=>x*x,-5,5))

    //  7、如何用reduceLeft得到数组中的最大元素?
    val arr=Array(2,4,6,8,2,3,5,7,9,0,19)
    val max = arr.reduceLeft((x:Int,y:Int)=>{if(x>y) x else y})
    println(max)

    //   8、用to和reduceLeft实现阶乘函数,不得使用循环或递归
    val factfun=(x:Int)=>{
      val factial = 1.to(x).reduceLeft(_*_)
      factial
    }

    println(factfun(5))

    //  9、编写函数largest(fun:(Int)=>Int,inputs:Seq[Int]),输出在给定输入序列中给定函数的最大值。举例来说，
    //  largest(x=>10x-xx,1 to 10)应该返回25.不得使用循环或递归**
    val largest=(fun:(Int)=>Int,inputs:Seq[Int])=>{
      val seq = inputs.map(fun)
      val maxval = seq.reduceLeft(math.max(_,_))

      maxval
    }
    println(largest(x=>10*x-x*x,1 to 10))

    //   10、修改前一个函数，返回最大的输出对应的输入。举例来说,largestAt(fun:(Int)=>Int,inputs:Seq[Int])应该返回5。
    // 不得使用循环或递归。

    val largestAt=(fun:(Int)=>Int,inputs:Seq[Int])=>{
      val arr = inputs.toArray
      val tuples = arr.map((x:Int)=>(x,fun(x)))
      val maxtu = tuples.reduceLeft((x:(Int,Int),y:(Int,Int))=>{if(math.max(x._2,y._2)==x._2) x else y})

      maxtu._1
    }
    println(largestAt(x=>10*x-x*x,1 to 10))

 }



}
