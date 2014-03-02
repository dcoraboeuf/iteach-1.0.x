package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;

import java.util.Locale;

public interface AccountService {

    Ack register(Locale locale, TeacherRegistrationForm form);

}
