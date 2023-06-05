package com.mzl.network.nio.code.groupchat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class GroupChatServer {

    private ServerSocketChannel listen;
    private Selector selector;

    private static final Integer PORT = 8090;

    // 初始化
    public GroupChatServer() {
        try {
            // 初始化通道
            this.listen = ServerSocketChannel.open();
            // 初始化选择器
            this.selector = Selector.open();
            // 绑定端口
            listen.bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listen.configureBlocking(false);
            listen.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 监听
     */
    public void listen() {
        try {
            while (true) {
                if (selector.select(2000) == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach((key) -> {
                    // 链接事件
                    if (key.isAcceptable()) {
                        try {
                            // 创建一个socket
                            SocketChannel socketChannel = listen.accept();
                            // 设置非阻塞模式
                            socketChannel.configureBlocking(false);
                            // 注册
                            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                            log.warn("用户:{}加入群聊", socketChannel.getRemoteAddress());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    // 读事件
                    if (key.isReadable()) {
                        readData(key);
                    }

                });
                // 清除
                selectionKeys.clear();

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                selector.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            // 获取channel
            socketChannel = (SocketChannel) key.channel();
            // 获取byteBuffer
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                // 本地显示
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                String message = new String(bytes);
                log.info("发言:{}", message);
                // 向其他客户端发送消息
                forward(message, socketChannel);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            try {
                // 取消注册
                if (socketChannel != null) {
                    log.error("用户:[{}]已下线,取消注册，关闭通道", ((SocketChannel) socketChannel).getRemoteAddress());
                    socketChannel.close();
                }
                key.cancel();
            } catch (IOException ioException) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void forward(String message, SocketChannel socketChannel) {
        log.info("服务器转发消息:[{}]", message);
        // 获取所有的channel 并排除自己
        Set<SelectionKey> keys = selector.keys();
        keys.forEach((key) -> {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && !targetChannel.equals(listen)) {
                try {
                    ((SocketChannel) targetChannel).write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
