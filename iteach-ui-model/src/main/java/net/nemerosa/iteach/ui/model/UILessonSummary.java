package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class UILessonSummary extends UIResource {

    private final int id;
    private final String href;
    private final int studentId;
    private final String studentName;
    private final String schoolName;
    private final String schoolColour;
    private final String location;
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final boolean hasComments;

    @ConstructorProperties({"id", "studentId", "studentName", "schoolColour", "schoolName", "location", "from", "to", "hasComments"})
    public UILessonSummary(int id, int studentId, String studentName, String schoolName, String schoolColour, String location, LocalDateTime from, LocalDateTime to, boolean hasComments) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.schoolName = schoolName;
        this.schoolColour = schoolColour;
        this.href = UILink.href("api/teacher/lesson/%d", id);
        this.location = location;
        this.from = from;
        this.to = to;
        this.hasComments = hasComments;
    }

    public String getTitle() {
        if (StringUtils.isBlank(location)) {
            return studentName;
        } else {
            return String.format("%s @ %s", studentName, location);
        }
    }

}
