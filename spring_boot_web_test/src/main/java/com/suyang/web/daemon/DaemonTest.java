package com.suyang.web.daemon;

public class DaemonTest {


    public static void main(String[] args) throws InterruptedException {
        DaemonThread thread = new DaemonThread();
        thread.setDaemon(true);
        thread.start();

        for (int i =  0 ; i < 10 ; i++) {
            Thread.sleep(200);
            System.out.println(Thread.currentThread().getName() + i);
        }
        thread.wait();
        thread.wait(10);
        thread.notify();
        thread.notifyAll();


    }
}
