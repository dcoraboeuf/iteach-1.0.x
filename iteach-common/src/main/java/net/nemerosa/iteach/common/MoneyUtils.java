package net.nemerosa.iteach.common;

import org.apache.commons.lang3.StringUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static Money computeIncome(Money hourlyRate, BigDecimal hours) {
        if (hourlyRate != null) {
            return hourlyRate.multipliedBy(hours, RoundingMode.HALF_UP);
        } else {
            return Money.zero(CurrencyUnit.EUR);
        }
    }

    public static Money addIncome(Money old, Money m) {
        if (old == null) {
            return m;
        } else if (m == null) {
            return old;
        } else if (old.getCurrencyUnit().equals(m.getCurrencyUnit())) {
            return old.plus(m);
        } else {
            return Money.zero(CurrencyUnit.of("XXX"));
        }
    }

    public static Money fromString(String value) {
        if (StringUtils.isNotBlank(value)) {
            if (StringUtils.containsOnly(value, "0123456789.")) {
                return Money.of(CurrencyUnit.EUR, new BigDecimal(value));
            } else {
                return Money.parse(value);
            }
        } else {
            return null;
        }
    }
}
