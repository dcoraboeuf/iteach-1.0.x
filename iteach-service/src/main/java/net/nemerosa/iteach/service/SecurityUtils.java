package net.nemerosa.iteach.service;

public interface SecurityUtils {

    int checkTeacher();

    int checkAdmin();

    String getCurrentAccountName();
}
