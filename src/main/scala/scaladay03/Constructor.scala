package scaladay03

//构造方法分为主构造方法和辅助构造方法，默认是一个无参的主构造方法
//主构造方法会执行类定义中的所有语句\表达式\代码块
//主构造方法定义，在类名后面定义构造方法的参数列表
//主构造方法中var，val修饰的参数，会自动成为类的属性
//没有var val修饰的的参数，不会成为累的属性，但是类中有引用，就会成为对象私有属性
//参数列表前面加private，表示主构造方法私有化了
class ConstructorDemo(private var name:String,val age:Int,gender:Char) {
  var weight:Double = 0.0
  def msg={
    //参数列表中的属性，没有val，var修饰，默认是val的
    println(gender)
  }
  println("ConstructorDemo")

  //定义辅助构造方法,方法名this，方法的第一句一定要调用主构造方法或其他辅助构造方法
  def this(name:String,age:Int,gender:Char,weight:Double){
    this(name,age,gender)
    this.weight = weight
  }

  //定义方法
  //同之前方法的规则


}

object testConstructorDemo{
  def main(args: Array[String]): Unit = {
    val cd = new ConstructorDemo("hhh",20,'女',50.0)
    println(cd.age)

    //println(cd.name)
    //println(cd.)

  }
}