package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.time.LocalDateTime;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;

public class UILessonFilterTest {

    @Test
    public void from_json() throws JsonProcessingException {
        assertJsonRead(
                UILessonFilter.builder()
                        .from(LocalDateTime.of(2014, 3, 16, 23, 0))
                        .to(LocalDateTime.of(2014, 3, 21, 23, 0))
                        .build(),
                object()
                        .with("from", "2014-03-16T23:00:00.000Z")
                        .with("to", "2014-03-21T23:00:00.000Z")
                        .end(),
                UILessonFilter.class
        );
    }

}
