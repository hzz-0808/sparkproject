package sparkday03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object collectionsDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("demo2").setMaster("local")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[Int] = sc.parallelize(List(1,2,3,4,5))

    val rdd2: RDD[Int] = sc.parallelize(List(1,2,3,4,5,6,7,8))
    //union要求两个rdd中的元素一致,合在一起之后没有去重
    val res1: RDD[Int] = rdd1.union(rdd2)

    println(res1.collect().toBuffer)

    //对res1中的元素去重
    val res2: RDD[Int] = res1.distinct()

    println(res2.collect().toBuffer)

    //返回两个集合的交集,要求两个rdd的元素类型一致
    val intersect: RDD[Int] = rdd1.intersection(rdd2)
    println(intersect.collect().toBuffer)

    //join,要求rdd的元素是key value类型的
    val rdd3: RDD[(String, Int)] = sc.parallelize(Array(("hhh",1),("ddd",4),("hdua",9),("yuyu",7)))
    val rdd4: RDD[(String, Int)] = sc.parallelize(Array(("fff",1),("ddd",4),("hdua",9),("disjf",7)))

    val res3: RDD[(String, (Int, Int))] = rdd3.join(rdd4)
    println(res3.collect().toBuffer)

    //左外连接，右外连接，逻辑如同mysql
    //连接得上，就是some，连接不上就是None
    val res4: RDD[(String, (Int, Option[Int]))] = rdd3.leftOuterJoin(rdd4)
    println(res4.collect().toBuffer)
    //全外连接
    val res5: RDD[(String, (Option[Int], Option[Int]))] = rdd3.fullOuterJoin(rdd4)
    println(res5.collect().toBuffer)

    //笛卡尔积
    val res6: RDD[((String, Int), (String, Int))] = rdd3.cartesian(rdd4)
    println(res6.collect().toBuffer)

    //可以对两个以上的rdd记性进行连接，结果集中的元素（key，value），
    //所有rdd中的key都会在结果集中出现
    //value是每一个rdd中每一个可以对应的value组成的可迭代集合的元组
    rdd3.cogroup(rdd4)
    sc.stop()
  }
}
