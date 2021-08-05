package Day2.optional;

import Day2.collection.User;

import java.util.Optional;

public class Main2 {

    public static void main(String[] args) {

        Optional<User> optionalUser = Optional.empty();
        optionalUser = Optional.of(new User(1, ""));

        // 값이 없다면 true
        optionalUser.isEmpty();
        // 값이 있다면 true
        optionalUser.isPresent();

        optionalUser.ifPresent(
                user -> {

                }
        );

        optionalUser.ifPresentOrElse(
                // 있다면 (Consumer)
                user -> { },
                // 없다면 (Runnable)
                () -> { }
        );

    }

}
