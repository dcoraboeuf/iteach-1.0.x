package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.service.model.Account;

import java.util.concurrent.Callable;

public interface SecurityUtils {

    int checkTeacher();

    int checkAdmin();

    String getCurrentAccountName();

    AccountAuthentication getCurrentAccount();

    boolean isLogged();

    <T> T asAccount(Account account, Callable<T> call) throws Exception;
}
