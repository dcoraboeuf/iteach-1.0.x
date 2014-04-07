package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Period;

import java.time.YearMonth;

public final class PeriodUtils {

    private PeriodUtils() {
    }

    public static Period toPeriod(YearMonth period) {
        Period p = new Period();
        p.setFrom(period.atDay(1).atStartOfDay());
        p.setTo(period.atEndOfMonth().atTime(23, 59));
        return p;
    }

}
