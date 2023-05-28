package com.mzl.network.nio;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        // 创建一个大小为5的buffer  可以存放5个buffer
        IntBuffer allocate = IntBuffer.allocate(5);
        // 向buffer中存放数据
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i * 2);
        }
        // 从buffer中读取数据
        // 将buffer 读写切换
        allocate.flip();
        allocate.position(2);
        allocate.limit(4);
        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
