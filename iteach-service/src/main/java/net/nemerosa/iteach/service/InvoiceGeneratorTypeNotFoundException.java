package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.InputException;

public class InvoiceGeneratorTypeNotFoundException extends InputException {
    public InvoiceGeneratorTypeNotFoundException(String type) {
        super(type);
    }
}
