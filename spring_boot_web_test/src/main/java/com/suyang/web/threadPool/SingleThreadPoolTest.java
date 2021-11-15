package com.suyang.web.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPoolTest {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            executor.submit(myThread);
        }
        System.out.println(Thread.currentThread().getName() + "主线程执行完毕");
    }
}
