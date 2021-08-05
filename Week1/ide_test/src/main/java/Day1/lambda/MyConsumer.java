package Day1.lambda;


@FunctionalInterface
public interface MyConsumer<T> {
    void consume(T t);
}
