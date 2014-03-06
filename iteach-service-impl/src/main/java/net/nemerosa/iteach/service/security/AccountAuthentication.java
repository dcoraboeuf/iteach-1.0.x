package net.nemerosa.iteach.service.security;

public interface AccountAuthentication {

    int getId();

    boolean isAdministrator();

    String getEmail();

}
