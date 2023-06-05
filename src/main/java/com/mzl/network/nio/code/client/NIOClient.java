package com.mzl.network.nio.code.client;

import com.sun.deploy.uitoolkit.impl.fx.AppletStageManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8090);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                log.warn("链接需要时间，客户端不会阻塞，可做其他工作");
            }
            // 如果连接成功，就发送数据
            String str = "哈哈哈";
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
            // byteBuffer写入到channel
            socketChannel.write(byteBuffer);
        }

//        socketChannel.socket().getInputStream().close();
//        socketChannel.socket().getOutputStream().close();
//        socketChannel.close();
    }
}
