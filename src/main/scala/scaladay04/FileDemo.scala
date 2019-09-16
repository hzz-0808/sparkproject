package scaladay04

import java.io.PrintWriter

import scala.io.Source

object FileDemo {

  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("G:\\Desktop\\广告投放项目.txt")
    //读取行
    val iterator = source.getLines()
    for(line <- iterator){
      println(line)
    }

    //读取单词

//    val words = source.mkString.split(" ")
//    for(word <- words){
//      println(word)
//    }
    //读取字母
//    for(c <- source){
//      println(c)
//    }

    //读取网络文件
//    val source = Source.fromURL("http://www.baidu.com")
//    val it = source.getLines()
//    for(line <- it)
//      println(line)

    val writer = new PrintWriter("writer.txt")
    for(line <- iterator){
      writer.println(line)
      writer.flush()
    }

    writer.close()
  }
}
