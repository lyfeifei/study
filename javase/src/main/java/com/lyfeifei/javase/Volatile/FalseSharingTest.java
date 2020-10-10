package com.lyfeifei.javase.Volatile;

import java.util.concurrent.Executors;

/**
 * 什么是伪共享
 * Cache Line大小是64Byte（linux下查看缓存行大小）。
 * 如果多个核的线程在操作同一个缓存行中的不同变量数据，那么就会出现频繁的 [缓存失效]，即使在代码层面看这两个线程操作的数据之间完全没有关系。
 * 这种不合理的资源竞争情况就是伪共享（False Sharing）
 */
public class FalseSharingTest {

    public static void main(String[] args) throws InterruptedException {
        testPointer(new Pointer());
    }

    private static void testPointer(Pointer pointer) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.x++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.y++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(System.currentTimeMillis() - start);
    }
}

class Pointer {
    // 避免伪共享： @Contended +  jvm参数：-XX:-RestrictContended
    //@Contended
    volatile long x;
    //避免伪共享： 缓存行填充
    //long p1, p2, p3, p4, p5, p6, p7;
    volatile long y;
}
