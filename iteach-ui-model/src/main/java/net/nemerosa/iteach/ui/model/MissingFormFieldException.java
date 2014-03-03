package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.InputException;

public class MissingFormFieldException extends InputException {
    public MissingFormFieldException(String field) {
        super(field);
    }
}
