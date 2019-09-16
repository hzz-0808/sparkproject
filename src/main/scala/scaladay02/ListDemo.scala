package scaladay02

import scala.collection.mutable.ListBuffer

object ListDemo {

  def main(args: Array[String]): Unit = {
    //不可变list
    var list = List('a','b','c')
    //空列表
    val emptylist = Nil
    val emptylist2:List[Nothing] = List()
    println(emptylist.isEmpty)
    println(emptylist2.isEmpty)
    println(emptylist2.getClass)

    val list1 = "dhsaj"::"jda"::"tom"::"jerry"::Nil //只有一个元素的list ::右结合
    println(list1)

    //所有的list=head+tail
    println(list1.head)
    println(list1.tail)

    //访问元素
    println(list1(0))
    for(elem<-list1)
      println(elem)

    //添加元素,添加到最前面
    list = 'd'::list
    println(list)

    list = 'e' +:list //+:必须把元素写前面
    list = list :+ 'f' //:+元素写后面，添加到list的尾部

    println(list)

    list = list ++ list
    println(list)

    list = list++:list
    println(list)

    list = list ::: list
    println(list)

    //删除
    list = list.drop(5)
    println(list)

    list = list.dropWhile((a:Char)=>a=='a')
    println(list)

    //拆分
    val list2 = List(10,20,30,40)
    val tuple:(List[Int],List[Int])= list2.splitAt(2)
    println(tuple._1)
    println(tuple._2)

    //分组
    val tuple1:(List[Int],List[Int])=list2.partition((x:Int)=>x%20==0)
    println(tuple1._1)
    println(tuple1._2)
    //通过模式匹配拆分
    val List(a,b,c,d) = list2
    println(a)
    println(d)

    val g::h=list2
    println(g)
    println(h)

    println("------------------------------------可变列表-------------------------------")

    val listBuffer = new ListBuffer[String]()
    //list2.copyToBuffer(listBuffer)
    listBuffer += "hhhh"
    listBuffer ++=Array("gggg","llll")
    println(listBuffer(0))

    //从头部删除
    listBuffer.drop(1)
    listBuffer.dropRight(2)
    println(listBuffer.take(1)) //从头部获取1个元素
    println(listBuffer.takeRight(3))

    listBuffer.sorted




  }
}
