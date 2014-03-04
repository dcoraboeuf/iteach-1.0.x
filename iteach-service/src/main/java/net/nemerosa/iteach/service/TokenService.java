package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.TokenKey;
import net.nemerosa.iteach.common.TokenType;

public interface TokenService {

    String generateToken(TokenType tokenType, String key);

    TokenKey checkToken(String token, TokenType tokenType);

    void consumesToken(String token, TokenType tokenType, String key);
}
