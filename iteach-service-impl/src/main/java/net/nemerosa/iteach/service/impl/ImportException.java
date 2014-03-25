package net.nemerosa.iteach.service.impl;

import net.sf.jstring.support.CoreException;

public class ImportException extends CoreException {
    public ImportException(Exception ex) {
        super(ex, ex);
    }
}
