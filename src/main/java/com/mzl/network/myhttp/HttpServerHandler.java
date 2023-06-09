package com.mzl.network.myhttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * SimpleChannelInboundHandler 继承自ChannelInboundHandlerAdapter
 * HttpObject 表示客户端和服务器端相互通信的数据被封装成HttpObject
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是一个httpRequest请求
        if (msg instanceof HttpRequest) {
            log.info("pipeline is :{}---{}", ctx.pipeline().hashCode(), this.hashCode());
            log.info("消息类型:{}", msg.getClass());
            log.info("客户端地址:{}", ctx.channel().remoteAddress());
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if (uri.getPath().equals("/favicon.ico")) {
                log.info("请求了图标，不做处理");
                return;
            }
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            ctx.channel().writeAndFlush(response);
        }
    }
}
