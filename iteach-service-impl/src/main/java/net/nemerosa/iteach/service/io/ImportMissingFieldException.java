package net.nemerosa.iteach.service.io;

import net.nemerosa.iteach.common.InputException;

public class ImportMissingFieldException extends InputException {
    public ImportMissingFieldException(String field) {
        super(field);
    }
}
