package net.nemerosa.iteach.ui.client;

import net.nemerosa.iteach.ui.model.UITeacher;

public interface UIClient {

    UITeacher login(String email, String password);

    void logout();

}
