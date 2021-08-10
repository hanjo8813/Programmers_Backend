package design_pattern.factory;

public class KakaoLogin implements Login{
    @Override
    public void login() {
        System.out.println("카카오 로그인!");
    }
}
