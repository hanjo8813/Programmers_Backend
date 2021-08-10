package com.programmers.java;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class TestClass implements Cloneable {
    private String a;
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

}
