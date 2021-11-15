package com.prgrms.devcourse.springsecuritymasterclass;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;

public class ThreadLocalApp {

    final static ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();

    public static void main(String[] args) {
        System.out.println(getCurrentThreadName() + " ### main set value = 1");
        threadLocalValue.set(1);
        a();
        b();

        // 메인 스레드가 아닌 다른 스레드에서 실행시키기
        CompletableFuture<Void> task = runAsync(() -> {
            a();
            b();
        });
        task.join();
    }

    public static void a() {
        Integer value = threadLocalValue.get();
        System.out.println(getCurrentThreadName() + " ### a() get value = " + value);
    }

    public static void b() {
        Integer value = threadLocalValue.get();
        System.out.println(getCurrentThreadName() + " ### b() get value = " + value);
    }

    public static String getCurrentThreadName(){
        return Thread.currentThread().getName();
    }


}
