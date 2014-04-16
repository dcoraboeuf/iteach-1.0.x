package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.service.model.*;

import java.util.Locale;
import java.util.stream.Stream;

public interface AccountService {

    String[] ACCEPTED_IMAGE_TYPES = {
            "image/jpeg",
            "image/png"
    };

    ID register(Locale locale, TeacherRegistrationForm form);

    Ack completeRegistration(Locale locale, String token);

    Account getAccount(int id);

    Stream<Account> getAccounts();

    Ack deleteAccount(int accountId);

    Profile getProfile();

    void saveProfile(Profile profile);

    Setup getSetup();

    Ack saveSetup(SetupForm form);

    Ack disableAccount(int accountId);

    Ack enableAccount(int accountId);

    Ack passwordChangeRequest(Locale locale);

    Ack passwordChange(String token, String oldPassword, String newPassword);

    Ack updateProfileCompanyLogo(Document file);
}
