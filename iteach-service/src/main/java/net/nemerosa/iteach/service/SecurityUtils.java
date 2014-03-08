package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.AccountAuthentication;

public interface SecurityUtils {

    int checkTeacher();

    int checkAdmin();

    String getCurrentAccountName();

    AccountAuthentication getCurrentAccount();

    boolean isLogged();
}
