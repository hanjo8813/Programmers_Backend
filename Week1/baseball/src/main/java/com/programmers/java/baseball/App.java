package com.programmers.java.baseball;

import com.programmers.java.baseball.engine.BaseBall;
import com.programmers.java.baseball.engine.NumberGenerator;
import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.Output;

public class App {
    public static void main(String[] args) {
        NumberGenerator generator = new HackFakerNumberGenerator();
        Console console = new Console();
        new BaseBall(generator, console, console).run();
    }
}
