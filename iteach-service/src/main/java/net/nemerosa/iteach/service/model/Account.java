package net.nemerosa.iteach.service.model;

import lombok.Data;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
public class Account {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;
    private final boolean verified;
    private final boolean disabled;

}
