package net.nemerosa.iteach.common;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AckTest {

    @Test
    public void ok() {
        assertTrue(Ack.OK.isSuccess());
    }

    @Test
    public void nok() {
        assertFalse(Ack.NOK.isSuccess());
    }

    @Test
    public void validate_true() {
        assertTrue(Ack.validate(true).isSuccess());
    }

    @Test
    public void validate_false() {
        assertFalse(Ack.validate(false).isSuccess());
    }

    @Test
    public void one_0() {
        assertFalse(Ack.one(0).isSuccess());
    }

    @Test
    public void one_1() {
        assertTrue(Ack.one(1).isSuccess());
    }

    @Test
    public void one_more() {
        assertFalse(Ack.one(2).isSuccess());
    }

    @Test
    public void and() {
        assertFalse(Ack.NOK.and(Ack.NOK).isSuccess());
        assertFalse(Ack.NOK.and(Ack.OK).isSuccess());
        assertFalse(Ack.OK.and(Ack.NOK).isSuccess());
        assertTrue(Ack.OK.and(Ack.OK).isSuccess());
    }

    @Test
    public void or() {
        assertFalse(Ack.NOK.or(Ack.NOK).isSuccess());
        assertTrue(Ack.NOK.or(Ack.OK).isSuccess());
        assertTrue(Ack.OK.or(Ack.NOK).isSuccess());
        assertTrue(Ack.OK.or(Ack.OK).isSuccess());
    }

}
