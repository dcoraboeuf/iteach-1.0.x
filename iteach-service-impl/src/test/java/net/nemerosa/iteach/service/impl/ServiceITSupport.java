package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;

import java.util.concurrent.Callable;

public interface ServiceITSupport {

    ID createTeacher(String name, String email);

    Ack completeRegistration(String email);

    ID createTeacherAndCompleteRegistration(String name, String email);

    <T> T asTeacher(int teacherId, Callable<T> call) throws Exception;
}
