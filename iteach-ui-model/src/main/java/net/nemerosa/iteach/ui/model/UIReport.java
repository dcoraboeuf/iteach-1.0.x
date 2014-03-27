package net.nemerosa.iteach.ui.model;

import lombok.Data;
import org.joda.money.Money;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
public class UIReport extends UIResource {

    private final String href;
    private final YearMonth period;
    private final String formattedPeriod;
    private final YearMonth periodBefore;
    private final YearMonth periodAfter;
    private final BigDecimal hours;
    private final Money income;
    private final List<UISchoolReport> schools;

    @ConstructorProperties({"period", "formattedPeriod", "periodBefore", "periodAfter", "hours", "income", "schools"})
    public UIReport(YearMonth period, String formattedPeriod, YearMonth periodBefore, YearMonth periodAfter, BigDecimal hours, Money income, List<UISchoolReport> schools) {
        this.href = UILink.href("api/teacher/report/%d/%d", period.getYear(), period.getMonthValue());
        this.period = period;
        this.formattedPeriod = formattedPeriod;
        this.periodBefore = periodBefore;
        this.periodAfter = periodAfter;
        this.hours = hours;
        this.income = income;
        this.schools = schools;
    }
}
