package scaladay03

class Person {
  //属性需要初始化
  var name = "tom"
  //var修饰的属性，属性都是默认私有的，但是会生成public的get，set方法
  //var修饰的属性，可以使用占位符_初始化，指定属性的数据类型
  //用null初始化时，要指定数据类型，否则会默认类型是Null
  var nickName:String = _
  val age0 = 0

  //val修饰的属性，都是私有的，但是只生成public的get方法，属性是final的
  //val age:Int = _
  //val修饰的属性不能使用占位符初始化


  private var gender = 0
  //私有属性，get,set方法都是私有的

  private [this] var weight= 50
  //对象私有属性，只有当前对象可以访问

  def compare(obj:Person)={
    this.age0-obj.age0
    //this.weight-obj.weight 对象私有属性，obj这个对象这里不能访问，因为没有get方法，只有当前这个对象可以访问
  }


}


object testPerson{
  def main(args: Array[String]): Unit = {
    //使用默认的无参构造方法，构建一个对象
    val obj = new Person
    //访问属性
    println(obj.age0)
    println(obj.name)
    println(obj.nickName)
    //不能访问weight，因为没有get set方法，不能访问gender，因为getset方法是私有的

    obj.name = "kkk"
    println(obj.name)
  }


}