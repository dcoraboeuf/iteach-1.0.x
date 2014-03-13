package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;

import static net.nemerosa.iteach.ui.model.UIFixtures.jsonTeacher;
import static net.nemerosa.iteach.ui.model.UIFixtures.teacher;
import static org.junit.Assert.assertEquals;

public class UITeacherTest {

    private final ObjectMapper mapper = ObjectMapperFactory.create();

    @Test
    public void to_json() throws JsonProcessingException {
        UITeacher t = teacher();
        assertEquals(
                mapper.writeValueAsString(t),
                mapper.writeValueAsString(jsonTeacher())
        );
    }

    @Test
    public void from_json() throws IOException {
        String json = mapper.writeValueAsString(jsonTeacher());
        assertEquals(
                mapper.readValue(json, UITeacher.class),
                teacher()
        );
    }

}
