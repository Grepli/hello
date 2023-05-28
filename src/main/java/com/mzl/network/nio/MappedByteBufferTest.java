package com.mzl.network.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1.直接在堆外内存修改文件，操作系统不需要拷贝一次
 * 适用于大文件
 */
public class MappedByteBufferTest {


    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\copy.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2：0：可以直接修改的起始位置
         * 参数3：5 ：映射到内存的大小
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
//        while (map.hasRemaining()){
//            System.out.println((char)map.get());
//        }
        map.put(0,(byte)'L');
        map.put(1,(byte)'L');
        map.put(2,(byte)'S');
        randomAccessFile.close();
    }

}
