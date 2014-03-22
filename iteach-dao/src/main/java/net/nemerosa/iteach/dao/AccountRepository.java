package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.model.TAccount;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface AccountRepository {

    int createAccount(AuthenticationMode mode, String identifier, String email, String name, String encodedPassword);

    TAccount findByEmail(String email);

    Ack accountVerified(int id);

    TAccount findUserByUsernameForPasswordMode(String email);

    TAccount getById(int id);

    Stream<TAccount> findAll();

    boolean checkPassword(int id, Predicate<String> check);

    TAccount findUserByUsernameForOpenIDMode(String identifier);
}
