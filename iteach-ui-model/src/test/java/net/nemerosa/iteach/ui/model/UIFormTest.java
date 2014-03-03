package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UIFormTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Strings strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH);

    @Test
    public void withName() {
        assertEquals("Test", UIForm.create().withName("Test").getName());
    }

    @Test
    public void withColour() {
        assertEquals("#FFFF00", UIForm.create().withColour("#FFFF00").getColour());
    }

    @Test
    public void simple_to_json() throws JsonProcessingException {
        assertEquals("{\"fields\":{\"name\":\"Test\",\"colour\":\"#FFFF00\"}}", mapper.writeValueAsString(UIForm.create().withName("Test").withColour("#FFFF00")));
    }

    @Test
    public void simple_from_json() throws IOException {
        assertEquals(
                UIForm.create().withName("Test").withColour("#FFFF00"),
                mapper.readValue("{\"fields\":{\"name\":\"Test\",\"colour\":\"#FFFF00\"}}", UIForm.class)
        );
    }

    @Test(expected = MissingFormFieldException.class)
    public void validation_name_null() {
        UIForm.create().getName();
    }

    @Test
    public void validation_name_blank() {
        validateNOK(
                () -> UIForm.create().withName("").getName(),
                "Incorrect value for field \"name\" - minimum length is 1"
        );
    }

    @Test
    public void validation_name_too_long() {
        validateNOK(
                () -> UIForm.create().withName(StringUtils.repeat("x", 81)).getName(),
                "Incorrect value for field \"name\" - maximum length is 80"
        );
    }

    private void validateNOK(Runnable task, String expectedMessage) {
        try {
            task.run();
            fail("Validation should have failed");
        } catch (InvalidFormFieldException ex) {
            assertEquals(expectedMessage, ex.getLocalizedMessage(strings, Locale.ENGLISH));
        }
    }

}
