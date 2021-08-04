package com.programmers.java.lambda;

public class Main {
    public static void main(String[] args) {

        MySupplier<String> s = () -> "Supplier";

        //MyMapper m = (str) -> str.length();
        MyMapper<String, Integer> m = String::length;
        MyMapper<Integer, Integer> m2 = i -> i * i;
        MyMapper<Integer, String> m3 = Integer::toHexString;

        //MyConsumer c = (i) -> System.out.println(i);
        MyConsumer<String> c = System.out::println;

        // 안쪽부터 살펴보면
        // 1. Supplier에서 String 생성
        // 2. Mapper m에서 해당 문자열 받고 길이 리턴
        // 3. m2에서 길이 받고 제곱 리턴
        // 4. m3에서 제곱 받고 16진수 리턴
        // 5. Consumer에서 그걸 출력
        // 6. 위의 과정을 Runnable로 수행
        MyRunnable r = () ->
                c.consume(
                        m3.map(
                                m2.map(
                                        m.map(
                                                s.supply()
                                        )
                                )
                        )
                );
        r.run();

    }
}
