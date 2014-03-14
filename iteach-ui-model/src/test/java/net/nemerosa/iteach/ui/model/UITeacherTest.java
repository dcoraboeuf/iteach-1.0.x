package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;
import static net.nemerosa.iteach.test.TestUtils.assertJsonWrite;
import static net.nemerosa.iteach.ui.model.UIFixtures.jsonTeacher;
import static net.nemerosa.iteach.ui.model.UIFixtures.teacher;

public class UITeacherTest {

    @Test
    public void to_json() throws JsonProcessingException {
        assertJsonWrite(
                jsonTeacher(),
                teacher()
        );
    }

    @Test
    public void from_json() throws IOException {
        assertJsonRead(
                teacher(),
                jsonTeacher(),
                UITeacher.class
        );
    }

}
