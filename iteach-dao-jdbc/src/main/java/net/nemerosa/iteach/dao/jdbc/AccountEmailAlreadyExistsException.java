package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.ConflictException;

public class AccountEmailAlreadyExistsException extends ConflictException {
    public AccountEmailAlreadyExistsException(String email) {
        super(email);
    }
}
