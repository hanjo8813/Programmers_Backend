package Day1.func;

class Greeting implements MySupply {
    @Override
    public String supply() {
        return "Hello World";
    }
}

class SayHello implements MyRunnable {
    @Override
    public void run() {
        System.out.println(new Greeting().supply());
    }
}

public class Main {
    public static void main(String[] args) {
        // 원래라면?
        // 1. String 변수 그릇 인터페이스가 있음
        // 2. 그걸 사용하려면 클래스에서 implements 해야함.
        // 3. 또 출력하는 함수 인터페이스가 있음
        // 4. 출력 함수 쓰려면 또 클래스에서 implements...
        // 5. 그 클래스에서 만들어진 String을 인자로 넣어줌
        // 6. main 함수에서 출력 클래스를 선언 -> 출력함수 불러줌

        // 이 과정을 일련의 과정으로 만들어보자
        // 인터페이스를 가져다가 즉석으로 Overriding => 익명 클래스 생성
        MyRunnable r = new MyRunnable() {
            @Override
            public void run() {
                MySupply s = new MySupply(){
                    @Override
                    public String supply() {
                        return "!!!!!!!";
                    }
                };
                System.out.println(s.supply());
            }
        };
        r.run();
    }
}
