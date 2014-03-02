package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.TokenType;

public interface TokenDao {

    void saveToken(TokenType type, String key, String token);

}
