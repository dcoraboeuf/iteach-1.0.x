package net.nemerosa.iteach.service;

public interface SecurityUtils {

    void checkTeacher(int teacherId);

    void checkAdmin();

    String getCurrentAccountName();
}
