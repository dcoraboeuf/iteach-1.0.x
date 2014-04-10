package net.nemerosa.iteach.dao;

import net.sf.jstring.support.CoreException;

import java.io.IOException;

public class InvoiceCannotReadException extends CoreException {

    public InvoiceCannotReadException(IOException ex) {
        super(ex);
    }
}
