package net.nemerosa.iteach.dao.jdbc;

public interface SQL {

    String ACCOUNT_ADMINISTRATOR_COUNT = "SELECT COUNT(ID) FROM ACCOUNT WHERE ADMINISTRATOR IS TRUE";
    String ACCOUNT_ID_BY_IDENTIFIER = "SELECT ID FROM ACCOUNT WHERE IDENTIFIER = :identifier";
    String ACCOUNT_ID_BY_EMAIL = "SELECT ID FROM ACCOUNT WHERE EMAIL = :email";
    String ACCOUNT_CREATE = "INSERT INTO ACCOUNT (ADMINISTRATOR, VERIFIED, MODE, IDENTIFIER, PASSWORD, EMAIL, NAME, DISABLED) VALUES (:administrator, :verified, :mode, :identifier, :password, :email, :name, FALSE)";

    String TOKEN_SAVE = "INSERT INTO TOKEN (TOKEN, TOKENTYPE, TOKENKEY, CREATION) VALUES (:token, :tokentype, :tokenkey, :creation)";

}