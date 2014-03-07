package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UIFormTest {

    private final ObjectMapper mapper = ObjectMapperFactory.create();
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

    @Test
    public void validation_color_ok() {
        assertEquals("#FFDD55", UIForm.create().withColour("#FFDD55").getColour());
    }

    @Test
    public void validation_color_nok_range() {
        validateNOK(
                () -> UIForm.create().withColour("#FFHH55").getColour(),
                "Incorrect value for field \"colour\" - must be a colour code like #FFDD44 (#[A-F0-9]{6})"
        );
    }

    @Test
    public void validation_color_nok_too_small() {
        validateNOK(
                () -> UIForm.create().withColour("#FF5").getColour(),
                "Incorrect value for field \"colour\" - must be a colour code like #FFDD44 (#[A-F0-9]{6})"
        );
    }

    @Test
    public void validation_color_nok_too_long() {
        validateNOK(
                () -> UIForm.create().withColour("#FF5FF5F").getColour(),
                "Incorrect value for field \"colour\" - must be a colour code like #FFDD44 (#[A-F0-9]{6})"
        );
    }

    @Test
    public void validation_hourlyRate_ok() {
        Money rate = UIForm.create().with("hourlyRate", "EUR 45.00").getHourlyRate();
        assertEquals(new BigDecimal("45.00"), rate.getAmount());
        assertEquals("EUR", rate.getCurrencyUnit().getCode());
    }

    @Test
    public void validation_hourlyRate_nok_currency() {
        validateNOK(
                () -> UIForm.create().with("hourlyRate", "45F 45.00").getHourlyRate(),
                "Incorrect value for field \"hourlyRate\" - not a valid amount (EUR 45.00 for example)"
        );
    }

    @Test
    public void validation_hourlyRate_nok_amount() {
        validateNOK(
                () -> UIForm.create().with("hourlyRate", "EUR mmm7").getHourlyRate(),
                "Incorrect value for field \"hourlyRate\" - not a valid amount (EUR 45.00 for example)"
        );
    }

    @Test
    public void validation_hourlyRate_nok_format() {
        validateNOK(
                () -> UIForm.create().with("hourlyRate", "45.00").getHourlyRate(),
                "Incorrect value for field \"hourlyRate\" - not a valid amount (EUR 45.00 for example)"
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
