package net.nemerosa.iteach.common;

public abstract class InputException extends CoreException {

    protected InputException(String message, Object... params) {
        super(message, params);
    }

}
