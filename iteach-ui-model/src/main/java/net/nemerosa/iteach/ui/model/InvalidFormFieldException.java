package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.InputException;
import net.sf.jstring.Localizable;

public class InvalidFormFieldException extends InputException {
    public InvalidFormFieldException(String field, String value, Localizable message) {
        super(field, value, message);
    }
}
