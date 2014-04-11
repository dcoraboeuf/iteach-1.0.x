package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.InputException;
import net.sf.jstring.Localizable;

public class InvoiceControlException extends InputException {
    public InvoiceControlException(Localizable localizable) {
        super(localizable);
    }
}
