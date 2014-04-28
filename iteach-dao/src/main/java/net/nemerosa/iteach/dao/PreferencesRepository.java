package net.nemerosa.iteach.dao;

import java.time.LocalTime;

public interface PreferencesRepository {

    boolean getBoolean(int teacherId, String name, boolean value);

    void setBoolean(int teacherId, String name, boolean value);

    LocalTime getTime(int teacherId, String name, LocalTime value);

    void setTime(int teacherId, String name, LocalTime value);
}
