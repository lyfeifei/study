package com.lyfeifei.javase.Volatile;

/**
 * JMM内存模型验证--volatile保证可见性测试
 */
public class VolatileTest {

    /**
     * 此处是否添加volatile,来验证内存模型
     *
     * 1、静态变量存在哪里？
     */
    private static boolean initFlag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("等待数据中....");
            while (!initFlag) {

            }
            System.out.println("--------------success-----------");
        }).start();

        Thread.sleep(3000);

        new Thread(() -> {
            prepareData();
        }).start();
    }

    public static void prepareData() {
        System.out.println("\n准备数据中....");
        initFlag = true; // 此处为第30行代码
        System.out.println("initFlag = " + initFlag);
        System.out.println("数据准备完成！");
    }
}
