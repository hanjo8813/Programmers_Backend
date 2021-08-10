package design_pattern.factory;

public class Main {
    public static void main(String[] args) {
        Factory loginFactory = new Factory();
        Login login1 = loginFactory.getLogin("Kakao");
        login1.login();
        Login login2 = loginFactory.getLogin("Naver");
        login2.login();
    }
}
