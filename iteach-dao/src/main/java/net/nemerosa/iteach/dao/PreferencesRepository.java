package net.nemerosa.iteach.dao;

public interface PreferencesRepository {

    boolean getBoolean(int teacherId, String name, boolean value);

    void setBoolean(int teacherId, String name, boolean value);

}
