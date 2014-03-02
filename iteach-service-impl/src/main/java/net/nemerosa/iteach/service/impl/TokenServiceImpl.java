package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.TokenDao;
import net.nemerosa.iteach.service.TokenService;
import net.nemerosa.iteach.common.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenDao tokenDao;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    @Transactional
    public String generateToken(TokenType type, String key) {
        // Generates the token
        String token = createToken(type, key);
        // Saves it
        tokenDao.saveToken(type, key, token);
        // OK
        return token;
    }

    private String createToken(TokenType type, String key) {
        String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
        return Sha512DigestUtils.shaHex(s);
    }

}
