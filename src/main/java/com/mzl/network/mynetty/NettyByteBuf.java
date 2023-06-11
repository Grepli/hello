package com.mzl.network.mynetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyByteBuf {

    // 客户端需要一个事件循环组即可
    public static void main(String[] args) throws InterruptedException {
        // 创建一个对象 包含一个byte[10]的数组
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        log.info("" + buffer.readableBytes());
        for (int i = 0; i < 10; i++) {
            log.info("" + buffer.readByte());
            log.info("读取一个过后:" + buffer.readableBytes());
        }
        for (int i = 12; i < 23; i++) {
            buffer.writeByte(i);
        }
        log.info("" + buffer.readableBytes());
        for (int i = 0; i < 10; i++) {
            log.info("" + buffer.readByte());
            log.info("读取一个过后:" + buffer.readableBytes());
        }

    }
}
