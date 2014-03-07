package net.nemerosa.iteach.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IDTest {

    private static final ObjectMapper mapper = ObjectMapperFactory.create();

    @Test
    public void success() {
        ID id = ID.success(10);
        assertTrue(id.isSuccess());
        assertEquals(10, id.getValue());
    }

    @Test
    public void failure() {
        ID id = ID.failure();
        assertFalse(id.isSuccess());
        assertEquals(-1, id.getValue());
    }

    @Test
    public void count_not1_with_id() {
        ID id = ID.count(0).withId(10);
        assertFalse(id.isSuccess());
        assertEquals(-1, id.getValue());
    }

    @Test
    public void count_1_with_id() {
        ID id = ID.count(1).withId(10);
        assertTrue(id.isSuccess());
        assertEquals(10, id.getValue());
    }

    @Test
    public void failure_to_json() throws JsonProcessingException {
        assertEquals("{\"success\":false,\"value\":-1}", mapper.writeValueAsString(ID.failure()));
    }

    @Test
    public void id_to_json() throws JsonProcessingException {
        assertEquals("{\"success\":true,\"value\":3}", mapper.writeValueAsString(ID.success(3)));
    }

    @Test
    public void failure_from_json() throws IOException {
        assertFalse(mapper.readValue("{\"success\":false,\"value\":-1}", ID.class).isSuccess());
    }

    @Test
    public void id_from_json() throws IOException {
        ID id = mapper.readValue("{\"success\":true,\"value\":3}", ID.class);
        assertTrue(id.isSuccess());
        assertEquals(3, id.getValue());
    }

}
