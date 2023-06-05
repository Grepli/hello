package com.mzl.network.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

@Slf4j
public class SelectorTest {


    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        selector.select();
        while (true){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
        }

    }
}
