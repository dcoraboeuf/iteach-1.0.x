package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * List of lessons for a student
 */
@Data
public class LessonReport {

    private final BigDecimal totalHours;
    private final BigDecimal periodHours;
    private final List<Lesson> lessons;

}
