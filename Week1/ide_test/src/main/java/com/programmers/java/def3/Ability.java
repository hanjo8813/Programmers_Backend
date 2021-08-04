package com.programmers.java.def3;

public interface Ability {
    // 스태틱 메소드도 가질 수 있다
    static void sayHello(){
        System.out.println("Hello Wolrd");
    }
}

interface Flyable {
    default void fly(){
        System.out.println("fly");
    };
}

interface Swimmable {
    default void swim(){
        System.out.println("swim");
    };
}

interface Walkable {
    default void walk(){
        System.out.println("walk");
    };
}