package scaladay02

import scala.collection.immutable

object TreeMapDemo {

  def main(args: Array[String]): Unit = {
    var treemap = new immutable.TreeMap[String,Int]()
    treemap += (("scala",5))
    treemap += ("java"->6)

    treemap ++= Array(("hadoop",3),("spark",4))

    println(treemap)
  }

}
