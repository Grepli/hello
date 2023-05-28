package com.mzl.network.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * 复制一个文件
 */
public class NIOFileChannel {

    public static void main(String[] args) throws IOException {
        // 创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lss\\.config\\clash\\logs\\2023-05-22-192123.log");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\copy.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // 创建数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = 0;
        while ((read = inputStreamChannel.read(byteBuffer)) != -1) {
            // 此时数组中已经有数据了 position 已经到了Limit位置 而接下来要把数据写到输出流中  需要反转
            byteBuffer.flip();
            // 数组写入通道
            outputStreamChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}