package scaladay03

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random


object MatchDemo {
  def main(args: Array[String]): Unit = {
    //常量匹配 变量匹配
    val a="scala1"
    val Pi=3.14
    a match{
      case b => println(b) //相当于把a的赋值给了b
      case "python" => println("python matched")
      case "scala" => println("scala matched")
      case "java" => println("scala matched")
      case _ => println("not matched")
    }

    val b = 0.0
    b match {
      case Pi=>println(Pi) //大写字母开头会被认为是常量，匹配不上
      case 8.88 => println("8.88")
      case _ => println("not matched")
    }

    val arr = Array(10,20,30)
    arr match {
      case Array(_,20,_) => println("middle 20")
      case Array(10,20,30) => println("all")
      case Array(10,_*) => println("start 10")
      case Array(x,y)=>println(x+":"+y)
      case _ =>println("not matched")
    }


    //类型匹配
    val arr1 =Array(10.8,"str",'g',Array(10),mutable.HashMap(("jjj",2),("kkk",8)))
    val elem = arr1(Random.nextInt(5))
    //println(elem)
    elem match {
      case a:Double => println("10.8")
      case b:String => println("str")
      case c:Char => println('g')
      case d:Array[Int]=>println(Array(10).toBuffer)
      case e:mutable.HashMap[String,Int] => println("HashMap")
      case _ => println("not matched")
    }


    //样例类匹配
    //样例类首先是一个类，一般没有方法
    //样例类自动生成apply UNapply方法，toString......
    //样例对象
    //case object PhoneMsg("zhangsan","hahhah","2019")
    abstract class Msg(title:String,content:String)
    //val obj = Msg("weather","raining")

    case class EmailMsg(title:String,content:String,date:String) extends Msg(title,content)

    case class SMSMsg(title:String,content:String,from:String) extends Msg(title,content)

  def method(msg:Msg): Unit ={
      msg match {
        case EmailMsg(title,content,date) => println(title+":"+content+"\t"+date)
        case SMSMsg(title,content,from) => println(title+":"+content+"\tfrom:"+from)
        case _ =>println("others")
      }
    }

    val obj = EmailMsg("weather","it is a sunning day","20190824")
    method(obj)


    val arr8 = Array(List(2,3,4), List(1,2,3), List(4,5,6))
    val list9 = arr8.fold(List(0,0,0))((x,y)=>(x(0)+y(0))::(x(1)+y(1))::(x(2)+y(2))::Nil).toArray
    println(list9.toBuffer)

    println(arr8(0))

    val source = Source.fromFile("D:/test.txt")
//    val lines = source.getLines()
//    var c =1
//    for(line <- lines){
//      val words = line.split("[ ,\\.]").filter(_!="")
//      println("("+c+","+words.length+")")
//      c+=1
//    }
    val strlist = List("aaa","bbb","ccc")
    val str1 = strlist.reduce(_+":"+_)
    println(str1)

    val string = source.mkString
    println(string)
    val words = string.split("[ ,\\.]").filter(_!="")

    println(words.toBuffer)
    val res = words.map((_,1)).groupBy(x=>x._1).map(x=>(x._1,x._2.length))
    println(res)


  }

}
