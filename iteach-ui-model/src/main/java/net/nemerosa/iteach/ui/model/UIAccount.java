package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIAccount extends UIResource<UIAccount> {

    private final int id;
    private final String email;
    private final boolean administrator;

    @Override
    public String getHref() {
        return UILink.href("api/account/%d", id);
    }
}
