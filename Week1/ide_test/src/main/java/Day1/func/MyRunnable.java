package Day1.func;

@FunctionalInterface
public interface MyRunnable {
    // 추상메소드가 하나박에 없는 인터페이스 == 함수형 인터페이스
    void run();
}

@FunctionalInterface
interface MyMap{
    // 포함하는 함수는 총 3개지만 추상메소드는 하나이므로 F-I 이다.
    void map();
    default void sayHello(){
        System.out.println("Hello World");
    }
    static void sayBye(){
        System.out.println("Bye");
    }
}