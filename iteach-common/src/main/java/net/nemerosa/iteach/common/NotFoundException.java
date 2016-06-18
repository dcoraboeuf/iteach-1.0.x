package net.nemerosa.iteach.common;

public abstract class NotFoundException extends InputException {

    protected NotFoundException(String message, Object... params) {
        super(message, params);
    }
}
