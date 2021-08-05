package Day1.lambda;

@FunctionalInterface
public interface MySupplier<T> {
    T supply();
}
