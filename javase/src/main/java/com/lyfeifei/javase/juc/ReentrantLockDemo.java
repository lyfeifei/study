package com.lyfeifei.javase.juc;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    final static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        reentrantLock.lock();

        reentrantLock.unlock();

        Thread.interrupted();
    }
}
