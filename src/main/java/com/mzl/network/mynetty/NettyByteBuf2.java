package com.mzl.network.mynetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class NettyByteBuf2 {

    // 客户端需要一个事件循环组即可
    public static void main(String[] args) throws InterruptedException {
        ByteBuf buffer = Unpooled.copiedBuffer("hello.world!", StandardCharsets.UTF_8);
        if (buffer.hasArray()) {
            byte[] array = new byte[buffer.readableBytes()];
            buffer.getBytes(buffer.readerIndex(), array);
            log.info("字节数组:{}", Arrays.toString(array));
            log.info("转义后的:{}", new String(array, StandardCharsets.UTF_8));
        }
        log.info("初始容量:{}", buffer.capacity());
        log.info("");


    }
}
