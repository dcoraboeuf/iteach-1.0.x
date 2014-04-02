package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class SetupForm {

    private final String email;
    private final String password;
    private final String passwordChange;

}
