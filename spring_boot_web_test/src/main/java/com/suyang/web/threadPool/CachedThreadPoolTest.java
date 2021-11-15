package com.suyang.web.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 1; i< 5 ; i++) {
            pool.submit(myThread);
            Thread.sleep(1000);
        }
    }
}
