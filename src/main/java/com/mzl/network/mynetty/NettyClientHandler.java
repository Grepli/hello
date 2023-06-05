package com.mzl.network.mynetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel is active:{}", ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello LiSi", CharsetUtil.UTF_8));
    }

    /**
     * 有读取事件时会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channel is read:{}", ctx);
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("channel message:{}", byteBuf.toString(CharsetUtil.UTF_8));
        log.info("channel remote address:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error:{}", cause.getCause(), cause);
        ctx.close();
    }
}
