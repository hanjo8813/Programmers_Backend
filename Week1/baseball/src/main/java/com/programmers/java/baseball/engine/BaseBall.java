package com.programmers.java.baseball.engine;

import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.Output;
import com.programmers.java.baseball.engine.model.BallCount;
import com.programmers.java.baseball.engine.model.Numbers;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class BaseBall implements Runnable{
    private final int COUNT_OF_NUMBERS = 3;

    private NumberGenerator generator;
    private Input input;
    private Output output;

    @Override
    public void run() {
        // 정답 숫자 생성
        Numbers answer = generator.generate(COUNT_OF_NUMBERS);

        // 게임 시작
        while(true){
            // 예측숫자(문자열) 입력
            String inputString = input.input("숫자를 맞춰보세요 : ");
            // str -> int 파싱
            Optional<Numbers> inputNumbers = parse(inputString);
            // 유효성 검사
            if(inputNumbers.isEmpty()){
                output.inputError();
                continue;
            }
            // 정답과 예측숫자 비교 후 클래스에 strike/ball 저장
            BallCount bc = ballCount(answer, inputNumbers.get());
            // 결과값을 알려주기
            output.ballCount(bc);
            // 만약 strike가 3개면 게임끝
            if(bc.getStrike() == COUNT_OF_NUMBERS){
                output.correct();
                break;
            }
        }
    }

    private BallCount ballCount(Numbers answer, Numbers inputNumbers) {

        // 이 변수들은 아래 람다식?에서 변경되어야한다
        // But! functional point에서는 scope이 제한적이다
        // 그래서?! scope 밖의 변수는 읽어오기만 가능하고 수정이 불가능함.
        // 따라서 wrapper 클래스 Atomic으로 변수를 선언해주어야 함
        AtomicInteger strike = new AtomicInteger();
        AtomicInteger ball = new AtomicInteger();

        answer.indexedforEach(
                (a, i) -> {
                    inputNumbers.indexedforEach(
                            (n, j) -> {
                                if(!a.equals(n))
                                    return;
                                if(i.equals(j))
                                    strike.addAndGet(1);
                                else {
                                    ball.addAndGet(1);
                                }
                            }
                    );
                }

        );
        return new BallCount(strike.get(), ball.get());
    }

    private Optional<Numbers> parse(String inputString) {
        // 빈 문자열은 cut
        if(inputString.length() != COUNT_OF_NUMBERS)
            return Optional.empty();

        // 1. chars() : string을 stream으로 하나씩 분리 -> intStream
        // 2. isDigit : char가 숫자인지 판단 후 bool 리턴 (intStream 인데..?)
        // 3. getNumericValue : char(intStream??) -> int 변환
        // 4. 중복제거 후 개수 세기
       // chars() : string -> intstream으로 변경
        long count = inputString.chars()
                .filter(Character::isDigit)
                .map(Character::getNumericValue)
                .filter(i -> i> 0)
                .distinct()
                .count();
        if(count != COUNT_OF_NUMBERS)
            return Optional.empty();
        return Optional.of(
                new Numbers(
                        inputString.chars()
                                .map(Character::getNumericValue)
                                .boxed()
                                .toArray(Integer[]::new)
                )
        );
    }
}
