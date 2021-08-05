package Day2.collection;

public class User {
    // 초기화를 위한 객체를 미리 만들어 놓는다.
    public static User EMPTY = new User(0, "");

    private int age;
    private String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public boolean isOver19(){
        return age >= 12;
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}
