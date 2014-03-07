package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.NotFoundException;

public class NoMessageFoundException extends NotFoundException {
    public NoMessageFoundException(String email) {
        super(email);
    }
}
