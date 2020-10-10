package com.lyfeifei.javase.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Demo3 {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        ExecutorService executorService1 = Executors.newFixedThreadPool(10);
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        ExecutorService executorService3 = Executors.newSingleThreadExecutor();

        ExecutorService executorService = new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

        for (int i = 0; i < 100; i++) {
            // submit最终还是会调用execute
            //executorService3.submit(new ThreadDemo3(i));
            executorService3.execute(new ThreadDemo3(i));
        }

        //Thread.currentThread().join();
        //System.out.println("执行时间：" + (System.currentTimeMillis() - start));
        executorService3.shutdown();
    }
}

class ThreadDemo3 implements Runnable {
    int i = 0;
    public ThreadDemo3(int i) {
        this.i = i;
    }
    @Override
    public void run() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(Thread.currentThread().getName() + "--" + i);
    }
}
