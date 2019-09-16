package scaladay02

object TupleDemo {

  def main(args: Array[String]): Unit = {
    //创建元组
    val tup1 = ("scala",100,'g',Array(1,3,5,7,9))
    println(tup1.getClass)

    val tup2 = new Tuple4[String,Int,Char,Array[Int]]("scala",100,'g',Array(1,3,5,7,9))

    //元素访问
    println(tup1._1) //_第几个元素，从1开始
    val tup3 = ("scala",("java","python",3)) //嵌套tuple
    println(tup3._2._3)
    println(tup3.productElement(1)) //从0开始计数

    //拉链操作
    val arr1 = Array("scala","java","python")
    val arr2 = Array(1,2,3,4)
    val res = arr1.zip(arr2)
    //val res0 = arr1 zip arr2
    println(res.toBuffer) //结果 ArrayBuffer((scala,1), (java,2), (python,3))

    val res2 = arr1.zipWithIndex
    println(res2.toBuffer)
    //ArrayBuffer((scala,0), (java,1), (python,2)),和所在下标做了一个映射

    val uuzip:(Array[String],Array[Int]) = res.unzip
    println(uuzip._1.toBuffer)
    println(uuzip._2.toBuffer)


    //遍历
    for(item <- tup3.productIterator){
      println(item)
    }
  }
}
