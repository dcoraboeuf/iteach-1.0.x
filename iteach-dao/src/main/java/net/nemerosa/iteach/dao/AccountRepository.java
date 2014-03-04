package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.model.TAccount;

public interface AccountRepository {

    int createAccount(AuthenticationMode mode, String identifier, String email, String name, String encodedPassword);

    TAccount findByEmail(String email);

    Ack accountVerified(int id);

    TAccount findUserByUsernameForPasswordMode(String email);

}
