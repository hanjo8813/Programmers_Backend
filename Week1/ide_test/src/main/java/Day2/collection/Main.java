package Day2.collection;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // MyCollection 클래스 객체 생성, 객체안에 String 리스트를 만든다
        MyCollection<String> c1 = new MyCollection<String>(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"));
        // map 사용해서 객체 리스트의 문자열을 길이 리스트로 바꾸기 (type도 바꿈)
        MyCollection<Integer> c2 = c1.map(String::length);
        c2.foreach(System.out::println);

        System.out.println("--------------");

        // 중간 객체 생략 -> 다 이어붙이기
        // filter 추가, predicate 메소드를 람다로 오버라이딩해서 넣어줌 (행위 지정)
        // MyCollection의 내부 메소드는 모두 MyCollection을 리턴함
        // 따라서 이렇게 메소드들을 이어나갈수가 있다 => 메소드 체이닝
        new MyCollection<String>(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"))
                .map(String::length)
                .filter(i -> i % 2 == 0)
                .foreach(System.out::println);

        System.out.println("--------------");

        // 리스트 출력 말고 리스트 사이즈 출력하기
        int s = new MyCollection<String>(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"))
                .map(String::length)
                .filter(i -> i % 2 == 0)
                .size();
        System.out.println(s);
    }
}
