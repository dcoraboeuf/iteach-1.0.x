package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.service.model.Student;

import java.util.concurrent.Callable;

public interface ServiceITSupport {

    ID createTeacher(String name, String email);

    Ack completeRegistration(String email);

    ID createTeacherAndCompleteRegistration(String name, String email);

    <T> T asTeacher(int teacherId, Callable<T> call) throws Exception;

    int createTeacherAndCompleteRegistration();

    int createSchool() throws Exception;

    int createSchool(int teacherId) throws Exception;

    Student createStudent() throws Exception;

    Student createStudent(int teacherId, int schoolId) throws Exception;
}
