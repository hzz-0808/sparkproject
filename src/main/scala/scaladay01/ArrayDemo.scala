package scaladay01

object ArrayDemo {
  def main(args: Array[String]): Unit = {

    //定长数组

    //创建定长数组

    val arr=Array(10,20,30,40)
    //括号中的数字表示当前数组的长度
    val arr1=new Array[Int](5)

    println(arr.length)
    println(arr1.length)

    //访问数组
    println(arr(0))

    //更新数组值
    arr(0)=1

    println(arr.toBuffer)


    //遍历数组
    for(i <- arr){
      println(i)
    }

    for(j <- 0 to arr.length-1){
      println(arr(j))
    }
  }

}
