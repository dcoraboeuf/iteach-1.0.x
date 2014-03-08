package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UITeacher extends UIResource<UITeacher> {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;

}
