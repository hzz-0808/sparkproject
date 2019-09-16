package scaladay03

class Employ extends Person{
  var salary:Double = 0

  def work={
    println("work...")
  }

  //overw重写父类方法，super引用父类方法
  override def compare(obj: Person): Int = super.compare(obj)


}


object ExtendsDemo {

  def main(args: Array[String]): Unit = {
    val employ = new Employ
    println(employ.getClass)
    //判断某个对象是否是某个类的实例或者对象
    println(employ.isInstanceOf[Person])

    val obj = employ.asInstanceOf[Person]

    println(obj.isInstanceOf[Person])
  }

}
