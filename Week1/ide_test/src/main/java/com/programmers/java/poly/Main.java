package com.programmers.java.poly;

public class Main {
    public static void main(String[] args) {
        // 호스트 코드 (최초 실행되는 곳)
        // 파라미터는 호스트에서 결정되야한다 구현객체에서는 타입?을 결정하지 않는다.
        new Main().run(LoginType.Kakao);
    }

    void run(LoginType loginType){
        // 타입을 getLogin함수에 줘서 타입에따라 하위 객체가 변동적으로 설계
        // 타입은 enum 클래스로 구현
        Login user = getLogin(loginType);
        user.login();
    }

    // factory 패턴
    private static Login getLogin(LoginType type) {
        if(type == LoginType.Kakao){
            return new KakaoLogin();
        }
        return new NaverLogin();
    }
}
