package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class Account {

    private final int id;
    private final int name;
    private final int email;
    private final boolean administrator;

}
