package scaladay04

import java.io.File

import scala.io.Source

//通过隐式转换方法实现功能的扩展
//希望给File，增加一个新的功能方法，read

class  RichFile(val file:File){
  def read = Source.fromFile(file.getPath).mkString

}

object RichFile{
  implicit def fileToRichFile(file:File)={
    println("implicit nethod is called")
    new RichFile(file)

  }
}

object ImplicitDemo {
  //通过隐式转换，优雅地扩展已有的库或者自定义类的功能
  //隐式转换方法，隐式转换类，隐式转换参数
  def main(args: Array[String]): Unit = {
    import RichFile._
    val res = new File("G:/Desktop/高效运营平台指标实现.txt").read


  }
//  import RichFile._
//  private val read: Any = new File("G:/Desktop/高效运营平台指标实现.txt").read

}


