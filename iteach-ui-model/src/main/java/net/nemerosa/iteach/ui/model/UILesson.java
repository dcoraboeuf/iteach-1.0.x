package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class UILesson extends UIResource {

    private final int id;
    private final String href;
    private final UIStudentSummary student;
    private final String location;
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final BigDecimal hours;
    private final boolean hasComments;

    @ConstructorProperties({"id", "student", "location", "from", "to", "hours", "hasComments"})
    public UILesson(int id, UIStudentSummary student, String location, LocalDateTime from, LocalDateTime to, BigDecimal hours, boolean hasComments) {
        this.id = id;
        this.href = UILink.href("api/teacher/lesson/%d", id);
        this.student = student;
        this.location = location;
        this.from = from;
        this.to = to;
        this.hours = hours;
        this.hasComments = hasComments;
    }

    public String getTitle() {
        String location = getLocation();
        String studentName = getStudent().getName();
        if (StringUtils.isBlank(location)) {
            return studentName;
        } else {
            return String.format("%s @ %s", studentName, location);
        }
    }

}
