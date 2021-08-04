package com.programmers.java.def;

// 추상메소드로만 이뤄진 클래스 == 인터페이스
interface MyInterface {
    // 추상 메소드
    void method1();

    // 구상 메소드
    // default method
    default void sayHello() {
        System.out.println("Hello World");
    }
}

public class Main implements MyInterface {

    public static void main(String[] args) {
        // default method는 Override없이 호출 가능
        // 물론 overrid도 가능
        new Main().sayHello();
    }

    @Override
    public void method1() {
        throw new RuntimeException();
    }
}
