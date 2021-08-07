package Day2.iterator;

import Day2.collection.MyCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyIterator<String> iter =
                new MyCollection<String>(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"))
                        .iterator();

        while(iter.hasNext()){
            String s = iter.next();
            int len = s.length();
            System.out.println(iter.next());
        }

        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4));
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }


    }
}
