package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.MoneyUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MoneyUtilsTest {

    @Test
    public void computeIncome() {
        Money income = MoneyUtils.computeIncome(Money.parse("EUR 25.50"), new BigDecimal("12.50"));
        assertEquals("EUR 318.75", income.toString());
    }

    @Test
    public void fromString_null() {
        assertNull(MoneyUtils.fromString(null));
    }

    @Test
    public void fromString_blank() {
        assertNull(MoneyUtils.fromString(""));
    }

    @Test
    public void fromString_complete() {
        assertEquals(
                Money.of(CurrencyUnit.USD, new BigDecimal("60.78")),
                MoneyUtils.fromString("USD 60.78"));
    }

    @Test
    public void fromString_partial() {
        assertEquals(
                Money.of(CurrencyUnit.EUR, new BigDecimal("45.20")),
                MoneyUtils.fromString("45.20"));
    }

}
