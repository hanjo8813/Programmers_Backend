package design_pattern.factory;

public class Factory {
    public Login getLogin(String loginType){
        if(loginType.equals("Kakao")){
            return new KakaoLogin();
        }
        else if(loginType.equals("Naver")){
            return new NaverLogin();
        }
        return null;
    }
}
