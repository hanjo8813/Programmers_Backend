package Day2.collection;

import Day2.iterator.MyIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MyCollection<T> {
    private List<T> list;

    public MyCollection(List<T> list) {
        this.list = list;
    }

    public MyCollection<T> filter(Predicate<T> predicate){
        List<T> newList = new ArrayList<>();
        // predicate 메소드 test : 파라미터값을 검증?한다
        // d의 검증결과가 참이라면 리스트에 추가
       foreach(d -> {
            if(predicate.test(d))
                newList.add(d);
        });
        return new MyCollection<>(newList);
    }

    // 이 메소드에서 제네릭을 U로 사용해라~ => U 타입은 이 메소드에서만 유효한 제네릭 타입
    // 1. 이 메소드는 MyCollection 타입이고, 파라미터로 function을 받음
    // 2. 이 인터페이스의 파라미터는 T, U이다
    // 3. U를 메소드내부에서 사용하려면 map 메소드 선언시 맨 앞에서 설정해줘야함
    public <U> MyCollection<U> map(Function<T, U> function){
        List<U> newList = new ArrayList<>();
        // function 메소드는 타입을 T->U로 변경해준다
        // 변경된 값을 -> 리스트에 추가하는 행위를 -> foreach consumer에 오버라이딩
        foreach(d -> newList.add(function.apply(d)));
        return new MyCollection<>(newList);
    }

    public int size(){
        return list.size();
    }

    public void foreach(Consumer<T> consumer) {
        for(int i=0; i<list.size(); i++){
            T data = list.get(i);
            consumer.accept(data);
        }
    }

    public MyIterator<T> iterator() {
        return new MyIterator<T>(){

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            @Override
            public T next() {
                return list.get(index++);
            }
        };
    }
}
