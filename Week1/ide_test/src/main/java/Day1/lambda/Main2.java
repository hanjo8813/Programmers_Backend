package Day1.lambda;

public class Main2 {
    public static void main(String[] args) {
        // 루프문 10번 돌려줘~ 그리고 print까지 해줘~
        new Main2().loop(10, System.out::println);
    }

    // 이 함수는 정말 loop만 돌려준다. / Consumer
    // 어떤 행위를 할지는 파라미터에 의존된다
    void loop(int n, MyConsumer<Integer> consumer) {
        for (int i = 0; i < n; i++) {
            consumer.consume(i);
        }
    }
}
