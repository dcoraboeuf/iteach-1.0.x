package net.nemerosa.iteach.acceptance.support;

import java.io.IOException;

public class UICannotAccessLinkException extends RuntimeException {
    public UICannotAccessLinkException(IOException ex, String link) {
        super(String.format("Cannot access link %s", link), ex);
    }
}
