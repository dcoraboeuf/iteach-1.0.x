package net.nemerosa.iteach.ui.model;

public class UIAccount extends UIResource<UIAccount> {

    private final String email;
    private final boolean administrator;

    public UIAccount(int id, String email, boolean administrator) {
        super(id);
        this.email = email;
        this.administrator = administrator;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdministrator() {
        return administrator;
    }
}
