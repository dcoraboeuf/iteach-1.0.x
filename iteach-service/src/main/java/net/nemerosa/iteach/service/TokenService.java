package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.TokenType;

public interface TokenService {

    String generateToken(TokenType tokenType, String key);

}
