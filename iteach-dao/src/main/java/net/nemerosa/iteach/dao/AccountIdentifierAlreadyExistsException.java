package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.ConflictException;

public class AccountIdentifierAlreadyExistsException extends ConflictException {
    public AccountIdentifierAlreadyExistsException(String identifier) {
        super(identifier);
    }
}
