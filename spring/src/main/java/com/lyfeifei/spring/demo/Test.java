package com.lyfeifei.spring.demo;

import com.lyfeifei.spring.bean.AnnotationConfigApplicationContext;
import com.lyfeifei.spring.bean.AppConfig;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(ac.getBean("userService"));
    }
}
