package scaladay02

import scala.collection.mutable.ListBuffer

object Homework02 {

  def main(args: Array[String]): Unit = {
    //创建一个List
    val list0 = List(1,7,9,8,0,3,5,4,6,2)
    //将list0中每个元素乘以10后生成一个新的集合

    val list1: List[Int] = list0.map(_*10)
    println(list1)

    //将list0中的偶数取出来生成一个新的集合
    val list2 = list0.filter((x:Int)=>x%2==0)
    //val list2 = list0.filter(_%2==0)
    println(list2)

    //将list0排序后生成一个新的集合
    val list3 = list0.sortWith((x:Int,y:Int)=>x<y)
    println(list3)

    //反转顺序
    val list4 = list0.sortWith((x:Int,y:Int)=>x<y).reverse
    println(list4)


    //将list0中的元素4个一组,类型为Iterator[List[Int]]
    val ite = list0.grouped(4)
    for(it <- ite){
      println(it)
    }

    //将Iterator转换成List
//
//    var list5:List[List[Int]] = Nil
//
//    for(it <- ite){
//      list5 = it::list5
//      println(list5)
//    }
//    println(list5)
    var list5 = ListBuffer[List[Int]]()
    ite.foreach((x:List[Int])=>list5 += x)
    println(list5.length)
    //ite.toList的结果是返回一个空的集合，上面的方法也是返回一个空的集合，不知道怎么回事

    val list8 = list0.grouped(4).toList
    println(list8)
    //这里就不是空的了，不知道咋回事






    //将多个list压扁成一个List
    val list6 = list5.flatten

    val lines = List("hello tom hello jerry", "hello jerry", "hello kitty")
    //先按空格切分，在压平
    val list7 = lines.map(_.split(" ")).flatten
    println(list7)

    //并行计算求和
    val arr = Array(1,2,3,4,5,6,7,8,9)
    val sum = arr.par.reduce((x:Int,y:Int)=>x+y)
    println(sum)

    //化简：reduce
    val sum1 = arr.par.reduce(_+_)
    println(sum1)

    //将非特定顺序的二元操作应用到所有元素
    val sum2 = arr.reduce(_+_)
    println(sum2)

    //安装特点的顺序
    val sum3 = arr.reduceLeft(_+_)
    println(sum3)

    //折叠：有初始值（无特定顺序）
    val ji = arr.fold(1)(_*_)
    println(ji)


    //折叠：有初始值（有特定顺序）
    val ji2 = arr.foldLeft(1)(_*_)
    println(ji2)


    //聚合
    val arr1 = List(List(1, 2, 3), List(3, 4, 5), List(2), List(0))
    val res = arr1.flatten.reduce(_+_)

    val l1 = List(5,6,4,7)
    val l2 = List(1,2,3,4)
    //求并集
    val s1 = l1.toSet
    val s2 = l2.toSet
    val res4 = s2 ++ s1
    println(res4)

    //求交集
    val res5 = s2 & s1
    println(res5)

    //求差集
    val res6 = s2 -- s1
    println(res6)
  }

}
