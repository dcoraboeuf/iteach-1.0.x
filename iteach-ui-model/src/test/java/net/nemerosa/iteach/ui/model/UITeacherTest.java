package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static org.junit.Assert.assertEquals;

public class UITeacherTest {

    private final ObjectMapper mapper = ObjectMapperFactory.create();

    @Test
    public void to_json() throws JsonProcessingException {
        UITeacher t = new UITeacher(
                1,
                "user",
                "user@test.com",
                false,
                AuthenticationMode.PASSWORD
        );
        assertEquals(
                mapper.writeValueAsString(t),
                mapper.writeValueAsString(
                        object()
                                .with("id", 1)
                                .with("name", "user")
                                .with("email", "user@test.com")
                                .with("administrator", false)
                                .with("authenticationMode", "PASSWORD")
                                .end()
                )
        );
    }

    @Test
    public void from_json() throws IOException {
        String json = mapper.writeValueAsString(
                object()
                        .with("id", 1)
                        .with("name", "user")
                        .with("email", "user@test.com")
                        .with("administrator", false)
                        .with("authenticationMode", "PASSWORD")
                        .end()
        );
        assertEquals(
                mapper.readValue(json, UITeacher.class),
                new UITeacher(
                        1,
                        "user",
                        "user@test.com",
                        false,
                        AuthenticationMode.PASSWORD
                )
        );
    }

}
