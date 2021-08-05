package Day1;

interface MyRunnable {
    void myRun();
}

interface YourRunnable {
    void yourRun();
}

public class Main implements MyRunnable, YourRunnable {
    public static void main(String[] args) {
        Main m = new Main();
        m.myRun();
        m.yourRun();
    }

    @Override
    public void myRun() {
        System.out.println("myRun");
    }

    @Override
    public void yourRun() {
        System.out.println("yourRun");
    }
}
