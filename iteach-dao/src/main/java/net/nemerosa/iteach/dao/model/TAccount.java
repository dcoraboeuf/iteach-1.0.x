package net.nemerosa.iteach.dao.model;

import lombok.Data;

@Data
public class TAccount {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;
    // TODO All other account fields but the password

}
