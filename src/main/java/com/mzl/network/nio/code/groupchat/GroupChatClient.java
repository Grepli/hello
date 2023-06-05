package com.mzl.network.nio.code.groupchat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

@Slf4j
public class GroupChatClient {

    private final String HOST = "127.0.0.1";

    private final Integer PORT = 8090;

    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;

    /**
     * 初始化
     */
    public GroupChatClient() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
            userName = socketChannel.getLocalAddress().toString().substring(1);
            log.info("系统已初始化");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendMessage(String info) {
        info = userName + ":" + info;
        try {
            ByteBuffer wrap = ByteBuffer.wrap(info.getBytes());
            socketChannel.write(wrap);
            wrap.clear();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void readMessage() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach((key) -> {
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        try {
                            channel.read(byteBuffer);
                            byte[] bytes = new byte[byteBuffer.position()];
                            byteBuffer.flip();
                            byteBuffer.get(bytes, 0, bytes.length);
                            log.info("{}", new String(bytes).trim());
                            byteBuffer.clear();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                });
                selectionKeys.clear();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }


    public static void main(String[] args) {
        // 启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(() -> {
            while (true) {
                groupChatClient.readMessage();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            log.info("读到的数据" + message);
            groupChatClient.sendMessage(message);
        }
    }
}
