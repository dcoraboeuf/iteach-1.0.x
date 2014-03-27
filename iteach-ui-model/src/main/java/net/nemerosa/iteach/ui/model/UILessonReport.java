package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.Period;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UILessonReport extends UIResource {

    private final int studentId;
    private final Period period;
    private final BigDecimal totalHours;
    private final BigDecimal periodHours;
    private final List<UILesson> lessons;

    @Override
    public String getHref() {
        return UILink.href("api/teacher/student/%d/lessons", studentId);
    }

}
