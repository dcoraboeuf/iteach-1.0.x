package net.nemerosa.iteach.dao.model;

import lombok.Data;
import net.nemerosa.iteach.common.TokenType;

import java.time.ZonedDateTime;

@Data
public class TToken {

    private final String token;
    private final TokenType type;
    private final String key;
    private final ZonedDateTime creation;

}
