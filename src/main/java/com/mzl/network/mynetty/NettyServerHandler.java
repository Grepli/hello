package com.mzl.network.mynetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 1.自定义handler，需要继承netty规定好的某个HandlerAdapter
 * 2. 这时我们的handler才能被netty处理
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据
     *
     * @param ctx 上下文对象：管道，通道，地址
     * @param msg 客户端发送的数据 默认是Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 耗费时间的业务->异步提交->提交该channel到对应的NioEventLoop中的taskQueue中
        // 解决方案1. 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("我做完了非常耗时的操作", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(20 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("我做完了非常耗时的操作2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(20 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("我做完了非常耗时的操作3", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        },5, TimeUnit.SECONDS);
        // 将msg转成ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("client:[{}] send Message is {}", ctx.channel().remoteAddress(), byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕
     * 数据写入到缓冲并刷新
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 一般讲 对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 一般需要关闭通道
        ctx.channel().close();
    }
}
