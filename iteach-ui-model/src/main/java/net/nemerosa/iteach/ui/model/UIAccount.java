package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIAccount extends UIResource {

    private final int id;
    private final String name;
    private final String email;
    private final AuthenticationMode authenticationMode;
    private final boolean administrator;
    private final boolean verified;
    private final boolean disabled;

    @Override
    public String getHref() {
        return UILink.href("api/account/%d", id);
    }
}
