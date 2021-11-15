package com.suyang.web.lock;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    static String[] str = {"ABC", "abc", "", ""};
    static int index = 2;

    public static void main(String[] args) throws InterruptedException {

        Lock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            str[index] = "hello";
            index++;
            lock.unlock();

        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            str[index] = "world";
            index++;
            lock.unlock();
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(Arrays.asList(str).toString());
    }
}
