package net.nemerosa.iteach.dao.model;

import lombok.Data;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
public class TAccount {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;
    private final boolean verified;
    private final boolean disabled;

}
