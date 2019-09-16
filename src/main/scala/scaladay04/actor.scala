package scaladay04

import scala.actors.{Actor, Future}


class actor1 extends Actor{
  override def act(): Unit = {
    println("actor1")

//    while (true){
//      receive{
//        case "start"=> {
//          println("start。。。")
//          Thread.sleep(1000)
//          println("started...")
//        }
//        case "stop"=>{
//          println("stoping...")
//          Thread.sleep(1000)
//          println("stoped")
//        }
//      }
//    }
//
//    loop{
//      react {
//        case "start" => {
//          println("start。。。")
//          Thread.sleep(1000)
//          println("started...")
//        }
//        case "stop" => {
//          println("stoping...")
//          Thread.sleep(1000)
//          println("stoped")
//        }
//      }
//    }

      loop {
        react {
          case "start" => {
            println("start。。。")
            Thread.sleep(2000)
            println("started...")
            sender ! "message received"
          }
          case "stop" => {
            println("stoping...")
            Thread.sleep(1000)
            println("stoped")
          }
        }
      }
  }
}

object ActorDemo1{
  def main(args: Array[String]): Unit = {
    val actor = new actor1
    actor.start()

    //发送消息同步，异步无返回值，异步有返回值
    //发送异步无返回值
//    actor !"start"
//    actor !"stop"

    //发送同步步有返回值
//    val message = actor !?"start"
//    println(message)

    //发送异步，有返回值
    val future:Future[Any] = actor !!"start"
    //判断是否已拿到结果
    if(future.isSet){
      println(future.apply())
    }else{
      println("reply not set")
    }

  }
}
