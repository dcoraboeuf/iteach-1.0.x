package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.model.TStudent;

import java.util.List;

public interface StudentRepository {

    int create(int teacherId, int schoolId, Integer contractId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email);

    TStudent getById(int teacherId, int studentId);

    List<TStudent> findAll(int teacherId);

    void disable(int teacherId, int studentId, boolean disabled);

    Ack delete(int teacherId, int studentId);

    Ack update(int teacherId, int studentId, int schoolId, Integer contractId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email);

    List<TStudent> findBySchool(int teacherId, int schoolId);
}
