package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.service.model.School;
import org.joda.money.Money;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MoneyUtilsTest {

    @Test
    public void computeIncome() {
        School school = new School(1, 1, "School", "", "", Money.parse("EUR 25.50"), "", "", "", "", "", "", null);
        Money income = MoneyUtils.computeIncome(school, new BigDecimal("12.50"));
        assertEquals("EUR 318.75", income.toString());
    }

}
