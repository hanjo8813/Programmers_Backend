package Day2.collection;

import java.util.Arrays;

public class Main2 {
    public static void main(String[] args) {
        new MyCollection<User>(
                Arrays.asList(
                        new User(10, "AAA"),
                        new User(11, "BBB"),
                        new User(12, "CCC"),
                        new User(13, "DDD"),
                        new User(14, "EEE"),
                        new User(15, "FFF")
                )
        )
                .filter(User::isOver19)
                .foreach(System.out::println);
    }
}
