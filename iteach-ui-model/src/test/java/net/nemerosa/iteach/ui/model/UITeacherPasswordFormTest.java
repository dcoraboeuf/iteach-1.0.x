package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UITeacherPasswordFormTest {

    @Test
    public void from_json() throws IOException {
        String json = "{\"name\":\"My Name\",\"email\":\"teacher@test.com\",\"password\":\"passw0rd\"}";
        UITeacherPasswordForm form = new ObjectMapper().readValue(json, UITeacherPasswordForm.class);
        assertEquals("My Name", form.getName());
        assertEquals("teacher@test.com", form.getEmail());
        assertEquals("passw0rd", form.getPassword());
    }

}
