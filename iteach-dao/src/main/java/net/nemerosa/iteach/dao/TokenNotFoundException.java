package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.NotFoundException;
import net.nemerosa.iteach.common.TokenType;

public class TokenNotFoundException extends NotFoundException {
    public TokenNotFoundException(String token, TokenType tokenType) {
        super(token, tokenType);
    }
}
