package Day2.optional;

import Day2.collection.User;

public class Main {

    public static void main(String[] args) {
        User user = User.EMPTY;
        User user2 = getUser();

        // 객체 == Null 로 확인하지 않고 정해진 초기값인지를 확인한다.
        if(user2 == User.EMPTY){

        }
    }

    private static User getUser() {
        return User.EMPTY;
    }
}
