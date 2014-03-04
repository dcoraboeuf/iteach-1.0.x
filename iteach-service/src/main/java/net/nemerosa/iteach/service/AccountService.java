package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.service.model.Account;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;

import java.util.Locale;

public interface AccountService {

    ID register(Locale locale, TeacherRegistrationForm form);

    Ack completeRegistration(Locale locale, String token);

    Account getAccount(int id);
}
