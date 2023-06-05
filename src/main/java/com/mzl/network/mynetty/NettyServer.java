package com.mzl.network.mynetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyServer {

    @SneakyThrows
    public static void main(String[] args) {
        // 创建bossGroup 和 workGroup
        // 创建了两个线程组
        // bossGroup只负责处理连接请求，和客户端交互的是workerGroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 两个都是无限循环
        // bossGroup 和workerGroup默认是CPU核数的2倍
        // 如果大于1  循环使用线程
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        // 创建服务器端启动对象--配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            // 设置线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置服务器通道的类型
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列等待链接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 保持活动链接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给workerEventLoop的对应的管道设置处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // handler加入到pipeline
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            log.info("server is running");
            // 绑定了一个端口  并且同步处理 生成了ChannelFuture
            ChannelFuture sync = serverBootstrap.bind(8090).sync();
            // 对关闭通道进行监听
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }

    }
}
