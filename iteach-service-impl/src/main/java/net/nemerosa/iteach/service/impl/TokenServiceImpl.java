package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.TokenKey;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.dao.TokenRepository;
import net.nemerosa.iteach.dao.TokenExpiredException;
import net.nemerosa.iteach.dao.TokenNotFoundException;
import net.nemerosa.iteach.dao.model.TToken;
import net.nemerosa.iteach.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private static final int EXPIRATION_DELAY = 15;
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String generateToken(TokenType type, String key) {
        // Generates the token
        String token = createToken(type, key);
        // Saves it
        tokenRepository.saveToken(type, key, token);
        // OK
        return token;
    }

    @Override
    public TokenKey checkToken(String token, TokenType tokenType) {
        try {
            TToken t = tokenRepository.findByTokenAndType(token, tokenType);
            TokenKey key = new TokenKey(t.getKey(), t.getCreation());
            ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
            long days = Duration.between(key.getCreation(), utcNow).toDays();
            if (days > EXPIRATION_DELAY) {
                throw new TokenExpiredException(token, tokenType, key);
            } else {
                return key;
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new TokenNotFoundException(token, tokenType);
        }
    }

    @Override
    public void consumesToken(String token, TokenType tokenType, String key) {
        // Checks the token
        checkToken(token, tokenType);
        // Deletes the token
        tokenRepository.deleteToken(tokenType, key);
    }

    private String createToken(TokenType type, String key) {
        String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
        return Sha512DigestUtils.shaHex(s);
    }

}
