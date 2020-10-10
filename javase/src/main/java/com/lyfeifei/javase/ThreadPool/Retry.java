package com.lyfeifei.javase.ThreadPool;

/**
 * retry 本意可能是跳出多层循环
 */
public class Retry {
    public static void main(String[] args) {
        retry:
        for (int i = 0; i < 3; i++) {
            System.out.println("外层循环执行：" +  i);
            for (int j = 0; j < 3; j++) {
                System.out.println("--内层循环执行：" +  j);
                if (i == 1)
                    break retry;
            }
        }
    }
}
