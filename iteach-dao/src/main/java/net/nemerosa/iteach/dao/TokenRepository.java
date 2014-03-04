package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.dao.model.TToken;

public interface TokenRepository {

    void saveToken(TokenType type, String key, String token);

    TToken findByTokenAndType(String token, TokenType tokenType);

    void deleteToken(TokenType tokenType, String key);
}
