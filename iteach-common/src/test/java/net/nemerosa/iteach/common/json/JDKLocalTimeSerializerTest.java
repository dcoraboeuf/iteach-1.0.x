package net.nemerosa.iteach.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;

public class JDKLocalTimeSerializerTest {

    @Test
    public void toJson() throws JsonProcessingException {
        Assert.assertEquals(
                "\"20:44\"",
                ObjectMapperFactory.create().writeValueAsString(LocalTime.of(20, 44))
        );
    }

    @Test
    public void fromJson() throws IOException {
        Assert.assertEquals(
                LocalTime.of(7, 30),
                ObjectMapperFactory.create().readValue("\"07:30\"", LocalTime.class)
        );
    }

}
