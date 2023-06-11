package com.mzl.network.myhttp;

import com.mzl.network.mynetty.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServer {


    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 该handler对应的是workerGroup
                    .childHandler(new HttpServerInitialize());
            log.info("服务启动成功,开始绑定端口");
            // 绑定了一个端口  并且同步处理 生成了ChannelFuture
            ChannelFuture sync = serverBootstrap.bind(8090).sync();
            // 判断有没有绑定成功 注册监听器
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("端口绑定成功,等待数据");
                    }
                }
            });
            // 对关闭通道进行监听
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

}
