package net.nemerosa.iteach.common;

public interface AccountAuthentication {

    int getId();

    String getName();

    String getEmail();

    boolean isAdministrator();

    AuthenticationMode getAuthenticationMode();

}
