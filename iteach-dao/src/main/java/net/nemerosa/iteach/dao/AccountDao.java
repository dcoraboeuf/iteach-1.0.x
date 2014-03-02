package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.AuthenticationMode;

public interface AccountDao {

    boolean isAdminInitialized();

    void createAccount(AuthenticationMode mode, String identifier, String email, String name, boolean administrator, boolean verified, String encodedPassword);

}
