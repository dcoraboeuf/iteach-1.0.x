package net.nemerosa.iteach.ui.client;

import net.nemerosa.iteach.ui.model.UITeacher;

public interface UIClient<C extends UIClient<C>> {

    UITeacher login();

    void logout();

    C withBasicLogin(String email, String password);

    C anonymous();
}
