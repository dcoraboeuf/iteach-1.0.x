package net.nemerosa.iteach.common;

public abstract class NotFoundException extends InputException {

    protected NotFoundException(Object... params) {
        super(params);
    }
}
