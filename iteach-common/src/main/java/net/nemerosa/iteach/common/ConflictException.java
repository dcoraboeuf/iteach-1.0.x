package net.nemerosa.iteach.common;

public abstract class ConflictException extends InputException {

    protected ConflictException(Object... params) {
        super(params);
    }
}
