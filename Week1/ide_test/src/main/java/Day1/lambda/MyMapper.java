package Day1.lambda;

@FunctionalInterface
public interface MyMapper<IN, OUT> {
    OUT map(IN s);
}
