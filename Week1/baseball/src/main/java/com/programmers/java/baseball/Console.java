package com.programmers.java.baseball;

import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.Output;
import com.programmers.java.baseball.engine.model.BallCount;

import java.util.Scanner;

public class Console implements Input, Output {

    Scanner scanner = new Scanner(System.in);

    @Override
    public String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public void ballCount(BallCount bc) {
        System.out.println(bc.getStrike() + " Strikes, " + bc.getBall() + " Balls");
    }

    @Override
    public void inputError() {
        System.out.println("입력 오류");

    }

    @Override
    public void correct() {
        System.out.println("정답입니다");
    }
}
