package com.lyfeifei.javase.Volatile;

/**
 * volatile 有序性
 *  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
 *  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly DCL
 *  -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=dontinline,*DCL.getInstance -XX:CompileCommand=compileonly,*DCL.getInstance -XX:+PrintAssembly
 */
public class DCL {

    private volatile static DCL instance;

    public static DCL getInstance() {
        if (instance == null) {
            synchronized (DCL.class) {
                if (instance == null) {
                    instance = new DCL();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        DCL.getInstance();
    }
}
