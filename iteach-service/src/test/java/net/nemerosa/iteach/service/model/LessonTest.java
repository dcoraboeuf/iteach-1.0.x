package net.nemerosa.iteach.service.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class LessonTest {

    @Test
    public void getDurationInHours_whole() {
        assertEquals(
                new BigDecimal("2.00"),
                new Lesson(1, 1, 1, "",
                        LocalDateTime.of(2014, 3, 27, 13, 0),
                        LocalDateTime.of(2014, 3, 27, 15, 0))
                        .getDurationInHours()
        );
    }

    @Test
    public void getDurationInHours_fraction_half() {
        assertEquals(
                new BigDecimal("2.50"),
                new Lesson(1, 1, 1, "",
                        LocalDateTime.of(2014, 3, 27, 13, 0),
                        LocalDateTime.of(2014, 3, 27, 15, 30))
                        .getDurationInHours()
        );
    }

    @Test
    public void getDurationInHours_fraction_quarter() {
        assertEquals(
                new BigDecimal("2.25"),
                new Lesson(1, 1, 1, "",
                        LocalDateTime.of(2014, 3, 27, 13, 0),
                        LocalDateTime.of(2014, 3, 27, 15, 15))
                        .getDurationInHours()
        );
    }

    @Test
    public void getDurationInHours_fraction_third() {
        assertEquals(
                new BigDecimal("2.33"),
                new Lesson(1, 1, 1, "",
                        LocalDateTime.of(2014, 3, 27, 13, 0),
                        LocalDateTime.of(2014, 3, 27, 15, 20))
                        .getDurationInHours()
        );
    }

}
