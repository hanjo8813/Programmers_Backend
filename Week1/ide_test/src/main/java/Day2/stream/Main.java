package Day2.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        Arrays.asList("a", "bb", "ccc", "dddd", "eeeee")
                .stream()
                .map(s -> s.length())
                .filter(i -> i % 2 == 1)
                .forEach(System.out::println);

    }


}
