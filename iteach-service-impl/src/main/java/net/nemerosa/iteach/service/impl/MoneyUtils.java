package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.service.model.School;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static Money computeIncome(School school, BigDecimal hours) {
        Money hourlyRate = school.getHourlyRate();
        if (hourlyRate != null) {
            return hourlyRate.multipliedBy(hours, RoundingMode.HALF_UP);
        } else {
            return Money.zero(CurrencyUnit.EUR);
        }
    }
}
