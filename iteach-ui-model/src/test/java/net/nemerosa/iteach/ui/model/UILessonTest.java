package net.nemerosa.iteach.ui.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class UILessonTest {

    @Test
    public void title_without_location() {
        assertEquals("The student", lesson("").getTitle());
    }

    @Test
    public void title_with_location() {
        assertEquals("The student @ The location", lesson("The location").getTitle());
    }

    private static UILesson lesson(String location) {
        return new UILesson(
                1,
                new UIStudentSummary(
                        1,
                        false,
                        new UISchoolSummary(
                                1,
                                "The school",
                                "#667788"
                        ),
                        "The student"
                ),
                location,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "",
                "",
                "",
                BigDecimal.ONE);
    }

}
