package net.nemerosa.iteach.ui.client;

public interface UIClient<C extends UIClient<C>> {

    void logout();

    C withBasicLogin(String email, String password);

    C anonymous();
}
