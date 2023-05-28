package com.mzl.network.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BIOServer {
    static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8090);
        serverSocket.setSoTimeout(1000);
        log.info("服务端启动成功");
        while (true){
            log.info("等待链接...");
            Socket accept = serverSocket.accept();
            log.info("有数据进来了");
            InputStream inputStream = accept.getInputStream();
            executorService.execute(()->{
                handler(accept);
            });
        }
    }

    public static void handler(Socket socket) {
        // 通过socket获取输入流
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                log.info("read...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    log.info(new String(bytes, 0, read));
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(bytes);
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                log.info("关闭和client的连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
