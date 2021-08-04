package com.programmers.java.def2;

public class Main {
    public static void main(String[] args) {

    }
}

// 인터페이스에 메소드가 두개 있는데 하나만 쓰고싶어..
// => 이럴 땐 Adapter라는 오버라이딩용 클래스를 만든 후 그 클래스를 상속받자!

// 하지만 이미 상속중인 클래스가 있다면..? => 결국 implements를 써야된다.. ㅜ
// => 그래서 자바 8부터는 인터페이스에 Default Method가 등장!

class Service extends Object implements MyInterface {

}
