package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.InputException;
import net.nemerosa.iteach.common.TokenKey;
import net.nemerosa.iteach.common.TokenType;

public class TokenExpiredException extends InputException {
    public TokenExpiredException(String token, TokenType tokenType, TokenKey key) {
        super(token, tokenType, key);
    }
}
