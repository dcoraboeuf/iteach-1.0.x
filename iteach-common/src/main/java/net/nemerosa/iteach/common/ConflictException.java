package net.nemerosa.iteach.common;

public abstract class ConflictException extends InputException {

    protected ConflictException(String message, Object... params) {
        super(message, params);
    }
}
