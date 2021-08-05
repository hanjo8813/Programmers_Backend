package Day1.func;

public class Main2 {
    public static void main(String[] args) {

        // 즉석 오버라이딩 + 익명클래스 생성 방식
        MyRunnable r1 = new MyRunnable(){
            @Override
            public void run() {
                System.out.println("@@@@@");
            }
        };

        // functional 인터페이스 + 람다를 사용하면 엄청나게 줄어든다.
        // 람다 : 익명 메소드를 사용해서 표현하는 것 (추상 메소드를 오버라이딩 하는 동작.)
        MyRunnable r2 = () -> System.out.println("#####");


    }
}
