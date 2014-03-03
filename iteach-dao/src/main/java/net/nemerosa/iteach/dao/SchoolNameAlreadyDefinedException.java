package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.ConflictException;

public class SchoolNameAlreadyDefinedException extends ConflictException {
    public SchoolNameAlreadyDefinedException(int teacherId, String name) {
        super(teacherId, name);
    }
}
