package com.mzl.network.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering: 聚合：将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering: 分散：将数据读入到buffer时，可以采用buffer数组，依次读出
 */
@Slf4j
public class ScaAndGaBuffer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8090);
        // 绑定端口到socket
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 创建buffer[]
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(3);
        byteBuffers[1] = ByteBuffer.allocate(5);
        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假定从客户端接受8个字节
        int messageLength = 8;
        while (true) {
            int readByte = 0;
            while (readByte < messageLength) {
                long read = socketChannel.read(byteBuffers);
                // 累计读取的字节数
                readByte += read;
                log.info("读取到的个数:{}", readByte);
                // 使用stream打印 当前buffer的position和limit
                Arrays.stream(byteBuffers)
                        .map(buffer -> "position:" + buffer.position() + ",limit:" + buffer.limit())
                        .forEach(log::info);
            }
            // 将所有的buffer反转
            Arrays.stream(byteBuffers).forEach(ByteBuffer::flip);
            // 数据读出显示到客户端
            long writeByte = 0;
            while (writeByte < messageLength) {
                long write = socketChannel.write(byteBuffers);
                writeByte += write;
            }
            // 所有的buffer复位
            Arrays.stream(byteBuffers).forEach(ByteBuffer::clear);
            log.info("readByte:[{}]", readByte);
            log.info("writeByte:[{}]", writeByte);
        }
    }
}
