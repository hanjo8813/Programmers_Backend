package design_pattern.memento;

public class Main {
    public static void main(String[] args) {
        CareTaker caretaker = new CareTaker();
        Originator origin = new Originator(1);
        System.out.println(origin.getNum());

        caretaker.push(origin.saveMemento());
        origin.setNum(2);
        System.out.println(origin.getNum());

        caretaker.push(origin.saveMemento());
        origin.setNum(3);
        System.out.println(origin.getNum());

        origin.restoreMemento(caretaker.pop());
        System.out.println(origin.getNum());

        origin.restoreMemento(caretaker.pop());
        System.out.println(origin.getNum());
    }
}
