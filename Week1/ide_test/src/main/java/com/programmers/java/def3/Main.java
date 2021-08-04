package com.programmers.java.def3;


class Duck implements Swimmable, Walkable {

}

class Swan implements Flyable, Walkable, Swimmable {

}

public class Main {
    public static void main(String[] args) {
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();
        new Swan().swim();
        Ability.sayHello();
    }
}
