package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;

public interface ServiceITSupport {
    Ack createTeacher(String name, String email);

    Ack completeRegistration(String email);

    Ack createTeacherAndCompleteRegistration(String name, String email);
}
