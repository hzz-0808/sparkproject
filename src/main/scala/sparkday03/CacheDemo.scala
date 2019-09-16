package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object CacheDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1 = sc.parallelize(List(1,3,5,7,9,2,4,6,8))

    val rdd2: RDD[Int] = rdd1.map((x: Int) => {
      println(x)
      x * 10
    })
    //多个action会触发多个job计算，如果没有缓存机制，
    // 上面定义的transformation都会计算两遍
    //有了缓存，就可以一次把结果计算出来，多次使用
    val cachedrdd: RDD[Int] = rdd2.cache()

    //什么时候缓存
    //减少重复计算，实现RDD的容错，提高程序执行效率
    //1）rdd在多次复用，进行缓存，目的是为了减少重复计算
    //2）计算逻辑复杂（计算依赖链特别长）（比如机器学习算法），就要将计算结果进行缓存
    //3）shuffle之后的rdd，进行缓存

    //    println(cachedrdd.collect().toBuffer)
//    println(cachedrdd.collect().toBuffer)

    println(rdd2.collect().toBuffer)
    println(rdd2.collect().toBuffer)

    val persistrdd: RDD[Int] = rdd2.persist()

    val cached: RDD[Int] = rdd2.persist(StorageLevel.MEMORY_ONLY)

  }
}
