package Day2.stream;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main2 {

    public static void main(String[] args) {
        // 리스트 -> Integer 스트림 만들기
        Stream<Integer> s = Arrays.asList(1,2,3).stream();

        System.out.println("--------------");

        // 배열(primitive type) 스트림 만들기
        IntStream s2 = Arrays.stream(new int[]{1,2,3});
        s2.boxed().collect(Collectors.toList()).forEach(System.out::println);

        System.out.println("--------------");

        // genrate로 스트림 만들기
        Random r = new Random();
        Stream.generate(() -> r.nextInt())
                .limit(10)
                .forEach(System.out::println);

        System.out.println("--------------");

        // iterator 로 스트림 만들기
        // iterator(초기값, Mapper)
        Stream.iterate(0, i -> i+1)
                .limit(10)
                .forEach(System.out::println);

    }
}
