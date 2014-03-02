package net.nemerosa.iteach.dao.jdbc;

import net.sf.jstring.support.CoreException;

import java.io.IOException;

public class JsonReadingException extends CoreException {
    public JsonReadingException(IOException e) {
        super(e, "Cannot read JSON: %s", e);
    }
}
