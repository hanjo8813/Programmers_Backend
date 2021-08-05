package Day2.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main3 {

    public static void main(String[] args) {

        // 주사위를 100번 던져 6이 나온 횟수 구하기
        Random r = new Random();
        var count = Stream.generate(() -> r.nextInt(6) + 1)
                .limit(100)
                .filter(n -> n == 6 )
                .count();
        System.out.println(count);

        // 1~9 사이 숫자를 중복없이 3개 담은 배열 출력
        int[] arr = Stream.generate(() -> r.nextInt(9) + 1)
                .distinct()
                .limit(3)
                .mapToInt(i -> i)
                .toArray();
        System.out.println(Arrays.toString(arr));

        // 0~200 사이 값 중 랜덤값 5개 뽑아 내림차순 표시
        int[] arr2 = Stream.generate(() -> r.nextInt(200))
                .limit(5)
                .sorted(Comparator.reverseOrder())
                .mapToInt(i -> i)
                .toArray();
        System.out.println(Arrays.toString(arr2));

    }
}
