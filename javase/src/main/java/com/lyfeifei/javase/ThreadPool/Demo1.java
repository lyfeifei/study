package com.lyfeifei.javase.ThreadPool;

public class Demo1 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadTest());
        Thread thread2 = new Thread(new ThreadTest());

        //thread1.run();
        thread2.start();
    }
}

class ThreadTest implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}