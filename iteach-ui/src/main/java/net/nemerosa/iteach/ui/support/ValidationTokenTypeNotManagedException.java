package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.TokenType;
import net.sf.jstring.support.CoreException;

public class ValidationTokenTypeNotManagedException extends CoreException {
    public ValidationTokenTypeNotManagedException(TokenType tokenType) {
        super(tokenType);
    }
}
