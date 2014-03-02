package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;

public interface AccountService {

    Ack register(TeacherRegistrationForm form);

}
