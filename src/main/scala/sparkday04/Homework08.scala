package sparkday04

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Homework08 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("homework").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)

    test1(sc)
  }

  /**
    * 1. 一共有多少人参加考试？
    * 1.1 一共有多少个小于20岁的人参加考试？
    * 1.2 一共有多少个等于20岁的人参加考试？
    * 1.3 一共有多少个大于20岁的人参加考试？
    */
  def test1(sc:SparkContext)={
    val rdd1: RDD[String] = sc.textFile("G:\\Spark\\spark\\day04\\data\\exam.txt")

    val mapedrdd: RDD[(String, (String, Int))] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(1), (strs(2), 1))
    })
    val groupedbyname: RDD[(String, Iterable[(String, Int)])] = mapedrdd.groupByKey()

    val person: RDD[Int] = groupedbyname.map((x:(String, Iterable[(String, Int)]))=>1)
    val cnt: Int = person.reduce(_+_)
    println(s"共有$cnt 个人参加考试")

    val persrdd: RDD[(Int, String)] = rdd1.map((x: String) => {
      val strs: Array[String] = x.split(" ")
      (strs(2).toInt, strs(1))
    })
    val res: RDD[(Int, String)] = persrdd.distinct().filter(_._1<20)
    val cnt2: Int = res.collect().length
    println(s"共有$cnt2 个小于20岁的人参加考试")

    val res1: RDD[(Int, String)] = persrdd.distinct().filter(_._1==20)
    val cnt3: Int = res1.collect().length
    println(s"共有$cnt3 个等于20岁的人参加考试")

    val res2: RDD[(Int, String)] = persrdd.distinct().filter(_._1>20)
    val cnt4: Int = res1.collect().length
    println(s"共有$cnt4 个大于20岁的人参加考试")

    /**
      * 2. 一共有多个男生参加考试？
      * 2.1 一共有多少个女生参加考试？
      */

    val sexrdd: RDD[(String, String)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(1), strs(3))
    })
    val cntmale: Int = sexrdd.distinct().filter(_._2=="男").collect().length
    println(s"一共有$cntmale"+"个男生参加考试")

    val cntfemale: Int = sexrdd.distinct().filter(_._2=="女").collect().length
    println(s"一共有$cntfemale"+"个女生参加考试")

    /**
      * 3. 12班有多少人参加考试？
      * 3.1 13班有多少人参加考试？
      */
    val classrdd: RDD[(String, String)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(0), strs(1))
    })
    val cnt12: Int = classrdd.distinct().filter(_._1=="12").collect().length
    println(s"一共有$cnt12"+"个12班的人参加考试")
    val cnt13: Int = classrdd.distinct().filter(_._1=="13").collect().length
    println(s"一共有$cnt13"+"个13班的人参加考试")

    /**
      * 4. 语文科目的平均成绩是多少？
      * 4.1 数学科目的平均成绩是多少？
      * 4.2 英语科目的平均成绩是多少？
      */

    val yuwenrdd: RDD[(String, Int)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(4), strs(5).toInt)
    })

    val yuwensturdd: RDD[(String, String)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(4), strs(1))
    })

    val score: RDD[(String, Int)] = yuwenrdd.reduceByKey(_+_)
    val pers: RDD[(String, Iterable[String])] = yuwensturdd.groupByKey()
    val cntrdd: RDD[(String, Int)] = pers.mapValues(_.toList.length)

    val mapedd: RDD[(String, (Int, Int))] = score.join(cntrdd)
    val resavg: RDD[(String, Double)] = mapedd.mapValues(x => {
      x._1.toDouble / x._2
    })
    println(resavg.collect().toBuffer)

    //5. 单个人平均成绩是多少？

    val perscore: RDD[(String, Int)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(1), strs(5).toInt)
    })
    val persubject: RDD[(String, String)] = rdd1.map((line: String) => {
      val strs: Array[String] = line.split(" ")
      (strs(1), strs(4))
    })
    val persum: RDD[(String, Int)] = perscore.reduceByKey(_+_)
    val subcnt: RDD[(String, Int)] = persubject.groupByKey().mapValues(_.toList.length)

    val joined: RDD[(String, (Int, Int))] = persum.join(subcnt)
    val peravg: RDD[(String, Double)] = joined.mapValues(x => {
      x._1.toDouble / x._2
    })
    println(peravg.collect().toBuffer)

    /**
      * 6. 12班平均成绩是多少？
      * 6.1 12班男生平均总成绩是多少？
      * 6.2 12班女生平均总成绩是多少？
      * 6.3 同理求13班相关成绩
      */
    val classScore: RDD[(String, Int)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      (strs(0), strs(5).toInt)
    })

    val classPerson: RDD[(String, String)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      (strs(0), strs(1))
    })
    val classSum: RDD[(String, Int)] = classScore.reduceByKey(_+_)
    val classPerCnt: RDD[(String, Int)] = classPerson.groupByKey().mapValues(_.toList.length)
    val joined3: RDD[(String, (Int, Int))] = classSum.join(classPerCnt)
    val classavg: RDD[(String, Double)] = joined3.mapValues(x => {
      x._1.toDouble / x._2
    })
    println(classavg.collect().toBuffer)

    val class_sex_score: RDD[((String, String), Int)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      ((strs(0), strs(3)), strs(5).toInt)
    })
    val class_sex_person: RDD[((String, String), String)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      ((strs(0), strs(3)), strs(1))
    })

    val class_sex_sum: RDD[((String, String), Int)] = class_sex_score.reduceByKey(_+_)
    val class_sex_cnt: RDD[((String, String), Int)] = class_sex_person.groupByKey().mapValues(_.toList.length)

    val joined4: RDD[((String, String), (Int, Int))] = class_sex_sum.join(class_sex_cnt)

    val class_sex_avg: RDD[((String, String), Double)] = joined4.mapValues(x => {
      x._1.toDouble / x._2
    })
    println(class_sex_avg.collect().toBuffer)

    /**
      * 7. 全校语文成绩最高分是多少？
      * 7.1 12班语文成绩最低分是多少？
      * 7.2 13班数学最高成绩是多少？
      */
    val all_score: RDD[(String, Int)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      (strs(4), strs(5).toInt)
    })

    val yuwen_max = all_score.filter(_._1 == "chinese").map(_._2).reduce(Math.max(_,_))
    println("全校语文成绩最高分:"+yuwen_max)

    val class_sub_score: RDD[((String, String), Int)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      ((strs(0), strs(4)), strs(5).toInt)
    })
    val yuwen_12_min: Int = class_sub_score.filter(_._1._1=="12").filter(_._1._2=="chinese").map(_._2).reduce(Math.min(_,_))
    println("12班语文成绩最低分："+yuwen_12_min)
    val shuxue_13_max: Int = class_sub_score.filter(_._1._1=="13").filter(_._1._2=="math").map(_._2).reduce(Math.max(_,_))
    println("13班数学成绩最高分："+shuxue_13_max)

    //8. 总成绩大于150分的12班的女生有几个？
    val class_sex_score2: RDD[((String,String, String), Int)] = rdd1.map(line => {
      val strs: Array[String] = line.split(" ")
      ((strs(0),strs(3), strs(2)), strs(5).toInt)
    })
    val sum_12_female: RDD[((String, String, String), Int)] = class_sex_score2.reduceByKey(_+_).filter(_._1._1=="12").filter(_._1._2=="女").reduceByKey(_+_)

    val sum_gt_150: Int = sum_12_female.filter(_._2>150).collect().length
    println("总成绩大于150分的12班的女生有"+sum_gt_150+"人")




    sc.stop()
  }

}
