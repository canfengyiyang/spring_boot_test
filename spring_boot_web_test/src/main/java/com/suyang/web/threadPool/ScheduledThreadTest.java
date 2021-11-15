package com.suyang.web.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledThreadTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyThread myThread = new MyThread();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 5; i++) {
            Future<?> submit = pool.submit(myThread);
            System.out.println(submit.get());
        }

    }
}
