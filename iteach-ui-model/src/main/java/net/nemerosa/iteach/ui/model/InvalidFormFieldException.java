package net.nemerosa.iteach.ui.model;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableException;

public class InvalidFormFieldException extends LocalizableException {
    public InvalidFormFieldException(String field, String value, Localizable message) {
        super(field, value, message);
    }
}
