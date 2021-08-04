package com.programmers.java.lambda;


@FunctionalInterface
public interface MyConsumer<T> {
    void consume(T t);
}
