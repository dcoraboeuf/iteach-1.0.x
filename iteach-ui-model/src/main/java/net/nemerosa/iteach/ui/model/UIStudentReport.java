package net.nemerosa.iteach.ui.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

/**
 * Report for a student over a period of time.
 */
@Data
public class UIStudentReport {

    private final int id;
    private final boolean disabled;
    private final String name;
    private final String subject;
    private final BigDecimal hours;
    private final Money hourlyRate;
    private final Money income;

    public String getHref() {
        return UILink.href("api/teacher/student/%d/report", id);
    }

    public UILink getStudent() {
        return UILink.of("Student", UILink.href("api/teacher/student/%d", id));
    }

}
