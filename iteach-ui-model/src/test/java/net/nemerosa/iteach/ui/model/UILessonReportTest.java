package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Collections;

import static net.nemerosa.iteach.common.json.JsonUtils.array;
import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;
import static net.nemerosa.iteach.test.TestUtils.assertJsonWrite;

public class UILessonReportTest {

    @Test
    public void to_json() throws JsonProcessingException {
        assertJsonWrite(
                jsonUILessonReport(),
                uiLessonReport()
        );
    }

    @Test
    public void from_json() throws JsonProcessingException {
        assertJsonRead(
                uiLessonReport(),
                jsonUILessonReport(),
                UILessonReport.class
        );
    }

    private UILessonReport uiLessonReport() {
        return new UILessonReport(
                1,
                YearMonth.of(2014, 3),
                YearMonth.of(2014, 2),
                YearMonth.of(2014, 4),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Collections.emptyList()
        );
    }

    private ObjectNode jsonUILessonReport() {
        return object()
                .with("studentId", 1)
                .with("href", "api/teacher/student/1/lessons/2014/3")
                .with("period", object().with("year", 2014).with("month", 3).end())
                .with("periodBefore", object().with("year", 2014).with("month", 2).end())
                .with("periodAfter", object().with("year", 2014).with("month", 4).end())
                .with("totalHours", 0)
                .with("periodHours", 0)
                .with("lessons", array().end())
                .end();
    }

}
