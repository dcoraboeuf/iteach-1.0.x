package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public class UISetup extends UIResource {

    private final String href;
    private final String email;

    @ConstructorProperties({"email"})
    public UISetup(String email) {
        this.href = "api/account/setup";
        this.email = email;
    }

}
