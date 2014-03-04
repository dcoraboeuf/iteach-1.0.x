package net.nemerosa.iteach.common;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class TokenKey {

    private final String key;
    private final ZonedDateTime creation;

}
