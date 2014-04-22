package net.nemerosa.iteach.ui.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

/**
 * Report for a school over a period of time.
 */
@Data
public class UISchoolReport {

    private final int id;
    private final String name;
    private final String colour;
    private final BigDecimal hours;
    private final Money income;
    private final Money incomeTotal;
    private final List<UIContractReport> contracts;

    public String getHref() {
        return UILink.href("api/teacher/school/%d/report", id);
    }

    public UILink getSchool() {
        return UILink.of("School", UILink.href("api/teacher/school/%d", id));
    }

}
