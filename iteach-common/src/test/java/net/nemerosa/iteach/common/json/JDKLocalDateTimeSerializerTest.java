package net.nemerosa.iteach.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class JDKLocalDateTimeSerializerTest {

    @Test
    public void toJson() throws JsonProcessingException {
        Assert.assertEquals(
                "\"2014-03-20T20:44:00Z\"",
                ObjectMapperFactory.create().writeValueAsString(LocalDateTime.of(2014, 3, 20, 20, 44))
        );
    }

    @Test
    public void fromJson_with_zone() throws IOException {
        Assert.assertEquals(
                LocalDateTime.of(2014, 3, 20, 7, 30),
                ObjectMapperFactory.create().readValue("\"2014-03-20T07:30:00.000Z\"", LocalDateTime.class)
        );
    }

    @Test
    public void fromJson_without_zone() throws IOException {
        Assert.assertEquals(
                LocalDateTime.of(2014, 3, 20, 7, 30),
                ObjectMapperFactory.create().readValue("\"2014-03-20T07:30:00.000\"", LocalDateTime.class)
        );
    }

    @Test
    public void fromJson_without_millis() throws IOException {
        Assert.assertEquals(
                LocalDateTime.of(2014, 3, 20, 7, 30),
                ObjectMapperFactory.create().readValue("\"2014-03-20T07:30:00\"", LocalDateTime.class)
        );
    }

    @Test
    public void fromJson_without_seconds() throws IOException {
        Assert.assertEquals(
                LocalDateTime.of(2014, 3, 20, 7, 30),
                ObjectMapperFactory.create().readValue("\"2014-03-20T07:30\"", LocalDateTime.class)
        );
    }

}
