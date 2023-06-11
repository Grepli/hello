package com.mzl.network.mynetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ServerDecoder extends ByteToMessageDecoder {



    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableIndex = byteBuf.readableBytes();
        byte[] bytes = new byte[readableIndex];
        byteBuf.getBytes(readableIndex,bytes);
        log.info(Arrays.toString(bytes));
        if (readableIndex<20){
            return;
        }
        log.info("业务处理");
    }
}
