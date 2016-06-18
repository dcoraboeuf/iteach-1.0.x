package net.nemerosa.iteach.common;

public abstract class CoreException extends RuntimeException {

    protected CoreException(String message, Object... params) {
        super(String.format(message, params));
    }

}
