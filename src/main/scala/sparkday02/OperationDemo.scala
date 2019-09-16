package sparkday02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object OperationDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("aperation demo")
      .setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    //创建一个RDD
    val rdd1:RDD[Int] = sc.parallelize(0 to 100,4)

    //val rdd2:RDD[String] = sc.textFile("G:\\Desktop\\高效运营平台指标实现.txt" )

    val filteredRDD: RDD[Int] = rdd1.filter(_%2==0)
    println(filteredRDD.collect().toBuffer)

    //map操作得到另一个rdd
//    val mapRdd: RDD[Int] = rdd1.map(_*10)
//
//    val arr: Array[Int] = mapRdd.collect()
//    println(arr.toBuffer)
    //释放资源

    //mapPartition
    //map和mapPartition的区别 mapPartition更高效，有可能导致OOM，内存充足的情况下考虑mapPartition

    //使用场景：比如写入数据库，一次连接，写入一个分区的数据，使用代用partition结尾的算子
    val mapPartition: RDD[Int] = rdd1.mapPartitions((it:Iterator[Int])=>it.map(_*10))
    println(mapPartition.collect().toBuffer)
    //mapPartitionWithIndex和mappartition的区别：基于分区数据进行计算，前者可以
    //获取数据对应的分区号
    //val rddwithindex: RDD[String] = rdd1.mapPartitionsWithIndex((index:Int, it:Iterator)=>it.map(index+":"+_))

    //flatmap之后rdd(0,0,1,0,1,2,...)
    val flatmaprdd = rdd1.flatMap((x:Int)=> 0 to x)
    println(flatmaprdd.collect().toBuffer)

    //foreach
    val unit: Unit = flatmaprdd.foreach(println)

    val rdd3 = sc.parallelize(List("scala","java","scala","java","python","c++"))
    //(key,compactBuffer(elem,elem))
    val rdd4: RDD[(String, Iterable[String])] = rdd3.groupBy((x:String)=>x)
    println(rdd4.collect().toBuffer)

    val mapedRDD: RDD[(String, Int)] = rdd3.map((_,1))
    val groupedRDD: RDD[(String, Iterable[Int])] = mapedRDD.groupByKey()
    println(groupedRDD.collect().toBuffer)

    val reduced: RDD[(String, Int)] = mapedRDD.reduceByKey(_+_)
    println(reduced.collect().toBuffer)


    val mappedWithIndex: RDD[String] = mapedRDD.mapPartitionsWithIndex((index:Int, it:Iterator[(String,Int)])=>it.map(index+":"+_))
    println(mappedWithIndex.collect().toBuffer)


    val folded: RDD[(String, Int)] = mapedRDD.foldByKey(100)(_+_)
    println(folded.collect().toBuffer)

    val aggregateByKeyed: RDD[(String, Int)] = mapedRDD.aggregateByKey(100)((x:Int, y:Int)=>Math.max(x,y), (a:Int, b:Int)=>a+b)
    println(aggregateByKeyed.collect().toBuffer)

    //排序
              val sortedby: RDD[(String, Int)] = aggregateByKeyed.sortBy((x:(                                                                                                     String,Int))=>x._2)
    aggregateByKeyed.sortByKey(false)
    sc.stop()
  }
}
