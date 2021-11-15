package com.suyang.web.threadPool;

public class MyThread implements Runnable {
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "我要⼀个教练");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadName + "教练来了：");
        System.out.println(threadName + "教完后，教练回到了游泳池");

    }
}
