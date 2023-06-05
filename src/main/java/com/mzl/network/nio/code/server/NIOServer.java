package com.mzl.network.nio.code.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8090));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 创建一个Selector
        Selector selector = Selector.open();
        // ServerSocketChannel注册到Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("注册后的selectionKey的数量:{}", selector.keys().size());
        while (true) {
            // 等于0就是没有链接事件发生
            if (selector.select(1000) == 0) {
//                log.info("服务器等待1s，但是没有客户端链接");
                continue;
            }
            // 如果返回的大于0就是有事件发生了
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 通过key反向获取通道
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 链接处理
                if (key.isAcceptable()) {
                    log.info("有新的客户端链接成功");
                    // 给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 将当前的SocketChannel注册到Selector 并且关注读事件 且给该channel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2));
                    log.info("注册后的selectionKey的数量:{}", selector.keys().size());
                }
                // 读 处理
                if (key.isReadable()) {
                    // 通过key获取channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 获取该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    byteBuffer.clear();
                    log.info("from 客户端 " + new String(byteBuffer.array()));
                }
                // 手动移除该selectionKey 防止重复操作
                iterator.remove();
            }
        }
    }
}
