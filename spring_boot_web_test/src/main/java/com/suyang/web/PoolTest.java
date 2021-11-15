package com.suyang.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorService executorService1 = Executors.newFixedThreadPool(10);
        Executors.newSingleThreadExecutor();
                  newSingleThreadPoolExecutor
    }
}
