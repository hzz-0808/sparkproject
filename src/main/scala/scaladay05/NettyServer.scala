package scaladay05

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class NettyServer {

  def bind(host:String,poot:Int)={

    //创建主从reactor线程池管理组
//    val mainGroup = new NioEventLoopGroup()
//
//    val subGroup = new NioEventLoopGroup()
//    //服务启动引导对象
//    val bootstrap = new ServerBootstrap()
//    bootstrap.group(mainGroup,subGroup)
//      .channel(classOf[NioServerSocketChannel])
//      .childHandler(new ChannelIn)//channel io处理




  }

}

