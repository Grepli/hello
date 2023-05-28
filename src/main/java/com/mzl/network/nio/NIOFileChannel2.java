package com.mzl.network.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 复制一个文件
 * version 2
 */
public class NIOFileChannel2 {

    public static void main(String[] args) throws IOException {
        // 创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lss\\.config\\clash\\logs\\2023-05-22-192123.log");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\copy.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // 创建数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 此时使用stream流 不能使用channel来判断
        if (fileInputStream.read() != -1){
            // 此时数组中已经有数据了 position 已经到了Limit位置 而接下来要把数据写到输出流中  需要反转
            outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());
        }
        inputStreamChannel.close();
        outputStreamChannel.close();
        // 关闭流
        fileInputStream.close();
        fileOutputStream.close();
    }
}