package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.ConflictException;

public class StudentNameAlreadyDefinedException extends ConflictException {
    public StudentNameAlreadyDefinedException(int teacherId, String name) {
        super(teacherId, name);
    }
}
