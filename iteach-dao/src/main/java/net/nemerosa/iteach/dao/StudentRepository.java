package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TStudent;

import java.util.List;

public interface StudentRepository {

    int create(int teacherId, int schoolId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email);

    TStudent getById(int teacherId, int studentId);

    List<TStudent> findAll(int teacherId);

    void disable(int teacherId, int studentId, boolean disabled);
}
