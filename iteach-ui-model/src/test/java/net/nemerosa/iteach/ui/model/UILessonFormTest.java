package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.time.LocalDateTime;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;
import static net.nemerosa.iteach.test.TestUtils.assertJsonWrite;

public class UILessonFormTest {

    @Test
    public void to_json() throws JsonProcessingException {
        assertJsonWrite(
                jsonUILessonForm(),
                uiLessonForm()
        );
    }

    @Test
    public void from_json() throws JsonProcessingException {
        assertJsonRead(
                uiLessonForm(),
                jsonUILessonForm(),
                UILessonForm.class
        );
    }

    public static ObjectNode jsonUILessonForm() {
        return object()
                .with("studentId", 12)
                .with("location", "Somewhere")
                .with("from", "2014-03-20T11:30:00")
                .with("to", "2014-03-20T14:00:00")
                .end();
    }

    public static UILessonForm uiLessonForm() {
        return UILessonForm.builder()
                .studentId(12)
                .location("Somewhere")
                .from(LocalDateTime.of(2014, 3, 20, 11, 30))
                .to(LocalDateTime.of(2014, 3, 20, 14, 0))
                .build();
    }

}
