package design_pattern.memento;

import java.util.Stack;

public class CareTaker {
    private Stack<Memento> mementoStack = new Stack<>();

    public void push(Memento memento){
        mementoStack.push(memento);
    }

    public Memento pop(){
        return  mementoStack.pop();
    }
}
