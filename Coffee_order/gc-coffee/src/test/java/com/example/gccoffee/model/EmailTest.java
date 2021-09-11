package com.example.gccoffee.model;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    public void testInvalidEamil() {
        assertThrows(IllegalArgumentException.class, ()->{
            var email = new Email("dfasdf");
        });
    }

    @Test
    public void testValidEamil() {
        var email = new Email("test@mail.com");
        assertThat(email.getAddress().equals("test@mail.com"), is(true));
    }

    @Test
    public void testEqualEamil() {
        var email = new Email("test@mail.com");
        var email2 = new Email("test@mail.com");
        assertThat(email.equals(email2), is(true));
    }

}