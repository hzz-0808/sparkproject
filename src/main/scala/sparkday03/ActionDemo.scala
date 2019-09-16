package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{Partition, SparkConf, SparkContext}

object ActionDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[Int] = sc.parallelize(List(1,2,3,4,5,6))
    val rdd2: RDD[Int] = rdd1.repartition(5)

    val res: Int = rdd1.reduce(_+_)

    println(res)

    val res2: Int = rdd2.fold(100)(_+_)
    //两个分区，每个分区聚合各个加上初始值，全局聚合再加上一次初始值，结果321
    println(rdd2.partitions.length)
    println(res2)

    val indexpar: RDD[String] = rdd2.mapPartitionsWithIndex((index:Int,it:Iterator[Int])=>it.map(index+":"+_))
    println(indexpar.collect().toBuffer)
    val res3: Int = rdd2.aggregate(100)(Math.max(_,_),_+_)
    println(res3)

    //collectAsMap() 需要是一个可以 value类型的集合
    val rdd4: RDD[(String, Int)] = rdd1.keyBy((x:Int)=>x+"aa")

    val res4: collection.Map[String, Int] = rdd4.collectAsMap()

    println(res4)

    val array: Array[(String, Int)] = rdd4.take(3)
    val array2: Array[(String, Int)] = rdd4.top(3)
    val array3: Array[(String, Int)] = rdd4.takeOrdered(2)
    val tuple: (String, Int) = rdd4.first()
    //first底层调用take(1),取出返回的数组的第一个元素

    println(array.toBuffer)
    println(array2.toBuffer)
    println(array3.toBuffer)
    println(tuple)
    //参数1：是否放回（有放回，就可以重复采样），参数2：采样个数，参数3：种子（有种子则就是一个伪随机）
    rdd4.takeSample(true,5,1000L)

    //返回元素个数
    rdd4.count()
    //返回每一key对应的value的个数
    val tupleToLong: collection.Map[(String, Int), Long] = rdd4.countByValue()

    //统计函数
//    val tuple: (String, Int) = rdd4.max()
//
//    val tuple: (String, Int) = rdd4.min()


  }
}
