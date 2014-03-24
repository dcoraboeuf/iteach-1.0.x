package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.InputException;

public class ImportVersionNotRecognizedException extends InputException {
    public ImportVersionNotRecognizedException(int version) {
        super(version);
    }
}
