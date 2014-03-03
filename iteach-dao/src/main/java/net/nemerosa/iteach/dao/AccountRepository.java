package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.AuthenticationMode;

public interface AccountRepository {

    void createAccount(AuthenticationMode mode, String identifier, String email, String name, String encodedPassword);

}
