package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.time.LocalTime;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;
import static net.nemerosa.iteach.test.TestUtils.assertJsonWrite;

public class UICalendarPreferencesTest {

    @Test
    public void to_json() throws JsonProcessingException {
        assertJsonWrite(
                jsonUICalendarPreferences(),
                uiCalendarPreferences()
        );
    }

    @Test
    public void from_json() throws JsonProcessingException {
        assertJsonRead(
                uiCalendarPreferences(),
                jsonUICalendarPreferences(),
                UICalendarPreferences.class
        );
    }

    private UICalendarPreferences uiCalendarPreferences() {
        return new UICalendarPreferences(
                LocalTime.of(8, 0),
                LocalTime.of(22, 0),
                true
        );
    }

    private ObjectNode jsonUICalendarPreferences() {
        return object()
                .with("minTime", "08:00")
                .with("maxTime", "22:00")
                .with("weekEnds", true)
                .end();
    }

}
