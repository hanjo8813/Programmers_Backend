package design_pattern.factory;

public class NaverLogin implements Login{
    @Override
    public void login() {
        System.out.println("네이버 로그인!");
    }
}
