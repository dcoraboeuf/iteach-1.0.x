package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;

import static net.nemerosa.iteach.ui.model.UIFixtures.jsonSchool;
import static net.nemerosa.iteach.ui.model.UIFixtures.school;
import static org.junit.Assert.assertEquals;

public class UISchoolTest {

    private final ObjectMapper mapper = ObjectMapperFactory.create();

    @Test
    public void from_json() throws IOException {
        assertEquals(
                UIFixtures.school(),
                mapper.readValue(
                        mapper.writeValueAsString(jsonSchool()),
                        UISchool.class
                )
        );
    }

    @Test
    public void from_json_no_hourly_rate() throws IOException {
        assertEquals(
                UIFixtures.school(null),
                mapper.readValue(
                        mapper.writeValueAsString(jsonSchool(null)),
                        UISchool.class
                )
        );
    }

    @Test
    public void to_json() throws JsonProcessingException {
        assertEquals(
                mapper.writeValueAsString(jsonSchool()),
                mapper.writeValueAsString(school())
        );
    }

    @Test
    public void to_json_no_hourly_rate() throws JsonProcessingException {
        assertEquals(
                mapper.writeValueAsString(jsonSchool(null)),
                mapper.writeValueAsString(school(null))
        );
    }

}
