package net.nemerosa.iteach.ui.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UISchoolFormTest {

    @Test
    public void toHourlyRate_null() {
        UISchoolForm form = UISchoolForm.builder()
                .hourlyRate(null)
                .build();
        assertNull(form.toHourlyRate());
    }

    @Test
    public void toHourlyRate_blank() {
        UISchoolForm form = UISchoolForm.builder()
                .hourlyRate("")
                .build();
        assertNull(form.toHourlyRate());
    }

    @Test
    public void toHourlyRate_complete() {
        UISchoolForm form = UISchoolForm.builder()
                .hourlyRate("USD 60.78")
                .build();
        assertEquals(
                Money.of(CurrencyUnit.USD, new BigDecimal("60.78")),
                form.toHourlyRate());
    }

    @Test
    public void toHourlyRate_partial() {
        UISchoolForm form = UISchoolForm.builder()
                .hourlyRate("45.20")
                .build();
        assertEquals(
                Money.of(CurrencyUnit.EUR, new BigDecimal("45.20")),
                form.toHourlyRate());
    }

}
