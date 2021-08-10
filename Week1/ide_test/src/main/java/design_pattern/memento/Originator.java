package design_pattern.memento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Originator {
    private int num;

    public Memento saveMemento(){
        return new Memento(num);
    }

    public void restoreMemento(Memento memento){
        num = memento.getNum();
    }
}
