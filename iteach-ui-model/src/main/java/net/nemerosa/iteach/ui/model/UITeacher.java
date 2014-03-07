package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UITeacher extends UIResource<UITeacher> {

    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;

    public UITeacher(int id, String name, String email, boolean administrator, AuthenticationMode authenticationMode) {
        super(id);
        this.name = name;
        this.email = email;
        this.administrator = administrator;
        this.authenticationMode = authenticationMode;
    }
}
