package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
public class UILessonReport extends UIResource {

    private final int studentId;
    private final String href;
    private final YearMonth period;
    private final String formattedPeriod;
    private final YearMonth periodBefore;
    private final YearMonth periodAfter;
    private final BigDecimal totalHours;
    private final BigDecimal periodHours;
    private final List<UILesson> lessons;

    @ConstructorProperties({"studentId", "period", "formattedPeriod", "periodBefore", "periodAfter", "totalHours", "periodHours", "lessons"})
    public UILessonReport(int studentId, YearMonth period, String formattedPeriod, YearMonth periodBefore, YearMonth periodAfter, BigDecimal totalHours, BigDecimal periodHours, List<UILesson> lessons) {
        this.studentId = studentId;
        this.period = period;
        this.formattedPeriod = formattedPeriod;
        this.periodBefore = periodBefore;
        this.periodAfter = periodAfter;
        this.totalHours = totalHours;
        this.periodHours = periodHours;
        this.lessons = lessons;
        this.href = UILink.href("api/teacher/student/%d/lessons/%d/%d", studentId, period.getYear(), period.getMonth().getValue());
    }

}
