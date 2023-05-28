package com.mzl.network.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(65);
        for (int i=0;i<64;i++){
            buffer.put((byte) i);
        }
        // 翻转
        buffer.flip();
        // 得到一个只读的buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        // 此处抛出异常
        readOnlyBuffer.put((byte) 1);


    }
}
