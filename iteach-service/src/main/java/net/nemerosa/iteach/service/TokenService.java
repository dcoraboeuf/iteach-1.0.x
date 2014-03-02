package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.TokenType;

public interface TokenService {

    String generateToken(TokenType tokenType, String key);

}
