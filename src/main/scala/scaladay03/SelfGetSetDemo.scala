package scaladay03

//自定义get set方法的实现
class SelfGetSetDemo {
  //属性名是以_开始的
  var _arr1:String = _
  //定义getset方法
  def arr1={
    _arr1
  }

  def arr1_(para:String)={
    if(!para.isEmpty){
      _arr1 = para
    }
  }

}
