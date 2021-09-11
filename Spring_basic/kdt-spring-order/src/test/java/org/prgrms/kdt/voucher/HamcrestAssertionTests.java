package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;


public class HamcrestAssertionTests {

    @Test
    @DisplayName("여러 hamcrest matcher 테스트")
    void hamcrestTest() {
        // 기대값, 테스트값
        assertEquals(2, 1+1);
        // 실제값 -> 테스트값, 기대값
        assertThat(1 +1, is(2));
        assertThat(1 +1, anyOf(is(2), is(2)));

        assertNotEquals(1, 1+1);
        assertThat(1+1, not(is(1)));
    }


    @Test
    @DisplayName("컬렉션에 대한 matcher 테스트")
    void hamcrestListMatcherTest() {
        var prices = List.of(1,2,3);
        assertThat(prices, hasSize(3));
        assertThat(prices, everyItem(greaterThan(0)));
        assertThat(prices, containsInAnyOrder(3,2,1));
        assertThat(prices, hasItem(1));
        assertThat(prices, hasItem(greaterThanOrEqualTo(2)));
    }

}
