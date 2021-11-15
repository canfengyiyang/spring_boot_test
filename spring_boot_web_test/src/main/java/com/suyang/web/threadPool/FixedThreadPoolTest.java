package com.suyang.web.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolTest {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            pool.submit(myThread);
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            System.out.println("线程池还未完全关闭");
        }
        System.out.println("线程池已经完全关闭");
    }
}
