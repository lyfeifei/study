package com.lyfeifei.javase.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableDemo implements Callable {

    private String param;

    public CallableDemo(String param) {
        this.param = param;
    }

    @Override
    public Object call() throws Exception {
        return "hello " + param;
    }

    public static void main(String[] args) throws Exception {
        FutureTask task = new FutureTask(new CallableDemo("Callable"));
        task.run();
        System.out.println(task.get());
    }
}
