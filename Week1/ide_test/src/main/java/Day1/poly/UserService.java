package Day1.poly;

public class UserService implements Login{
    // 인터페이스(추상체)와 관계를 맺는다? => 결합도가 낮아짐

    // 로그인에 의존적이고, 로그인 종류가 정해지지 않음
    private Login login;

    // 생성자!
    // 의존성을 외부에 맡겨서 의존도를 낮춤 => 의존성 주입 (Dependency Injection DI)
   public UserService(Login login) {
        this.login = login;
    }

    @Override
    public void login() {
        login.login();
    }
}
