package com.lyfeifei.javase.jvm;

public class JvmTest1 {

    static Test2 t1 = new Test2();

    Test2 t2 = new Test2();

    public void fn() {
        Test2 t3 = new Test2();
    }
}

class Test2 {

}
