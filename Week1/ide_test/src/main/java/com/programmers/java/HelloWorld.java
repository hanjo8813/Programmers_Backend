package com.programmers.java;

public class HelloWorld{
    public static void main(String[] args){
        TestClass a = new TestClass("HI");
        System.out.println(a);
        TestClass b = a;
        System.out.println(b);

        a.setA("BYE");

        System.out.println(a.getA().hashCode());
        System.out.println(b.getA().hashCode());


    }
}
