package net.nemerosa.iteach.dao.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jstring.support.CoreException;

public class JsonWritingException extends CoreException {
    public JsonWritingException(JsonProcessingException e) {
        super(e, "Cannot convert to JSON: %s", e);
    }
}
