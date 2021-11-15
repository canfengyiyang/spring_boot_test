package com.suyang.web.threadPool;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    @Override
    public String call() {
        System.out.println(Thread.currentThread().getName() + "执行Callable线程");
        return "执行成功";
    }
}
