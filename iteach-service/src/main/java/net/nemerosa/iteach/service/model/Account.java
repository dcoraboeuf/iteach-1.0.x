package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class Account {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;

}
