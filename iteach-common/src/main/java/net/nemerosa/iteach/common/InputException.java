package net.nemerosa.iteach.common;

import net.sf.jstring.support.CoreException;

public abstract class InputException extends CoreException {

    protected InputException(Object... params) {
        super(params);
    }

}
