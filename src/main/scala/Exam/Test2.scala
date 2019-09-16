package Exam

import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql._

case class emp1(empno:Int,deptno:Int)
case class dept1(deptno:Int,dname:String)
object Test2 {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder()
      .appName("Exam2")
      .master("local[2]")
      .getOrCreate()

    import  org.apache.spark.sql.functions._
    import sparkSession.implicits._
    val dept_src: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\Spark2\\dept.txt")
    val emp_src: Dataset[String] = sparkSession.read.textFile("G:\\Spark\\Spark2\\emp.txt")

    //1.列出至少有一个员工的所有部门【5分】
    val dept_map: Dataset[dept1] = dept_src.map(line => {
      val arr: Array[String] = line.split(",")
      //println(arr.toBuffer)
      dept1(arr(0).toInt, arr(1))
    })
    val emp_map: Dataset[emp1] = emp_src.map(line => {
      val arr: Array[String] = line.split(",")
      emp1(arr(0).toInt, arr(7).toInt)
    })
    val dept_df: DataFrame = dept_map.toDF()
    val emp_df: DataFrame = emp_map.toDF()
    val res1: DataFrame = emp_df.join(dept_df,"deptno")
    val res2: Dataset[Row] = res1.groupBy("deptno").agg(count("empno") as "counts").filter("counts>=1")
    res2.show()

    //2.列出与"周八"从事相同工作的所有员工【5分】
    val maped2: Dataset[(String, String)] = emp_src.map(line => {
      val strs: Array[String] = line.split(",")
      (strs(1), strs(2))
    })
    val df2: DataFrame = maped2.toDF("ename","job")
    //df2.show()
    val res3: DataFrame = df2.filter("ename='周八'").join(df2,"job")
    res3.show()
    //3.将所有员工按照名字首字母升序排序，首字母相同的按照薪水降序排序。【5分】
    val maped4: Dataset[(String, Int)] = emp_src.map(line => {
      val strs: Array[String] = line.split(",")
      (strs(1), strs(5).toInt)
    })
    val df3: DataFrame = maped4.toDF("ename","sal").selectExpr("substring(ename,0,1) as firstname","ename","sal")
    val res4: Dataset[Row] = df3.sort($"ename" desc, $"sal" asc)
    res4.show()

    //4.删除10号部门薪水最高的员工。【5分】

    val maped5: Dataset[(String, Int,Int)] = emp_src.map(line => {
      val strs: Array[String] = line.split(",")
      (strs(1), strs(5).toInt,strs(7).toInt)
    })
    val df5: DataFrame = maped5.toDF("ename","sal","deptno")
    val windowSpec: WindowSpec = Window.partitionBy("deptno").orderBy($"sal" desc)
    val rn: Column = rank.over(windowSpec).as("rn")
    val res: DataFrame = df5.select(col("ename"),col("deptno"),rn)
    val res10: Dataset[Row] = res.filter(!expr("deptno='10' and rn=1"))
    res10.show()


    //5.查出emp表中所有部门的最高薪水和最低薪水，部门编号为10的部门不显示【5分】
    val maped8: Dataset[(Int, String)] = emp_src.map(line => {
      val arr: Array[String] = line.split(",")
      (arr(5).toInt, arr(7))
    })
    val df10: DataFrame = maped8.toDF("sal","deptno")
    val res6: Dataset[Row] = df10.groupBy("deptno").agg(max("sal") as "maxsal",min("sal") as "minsal").filter("deptno!='10'")

    res6.show()
    //6.将薪水最高的员工的薪水降30%。【5分】


  }

}
