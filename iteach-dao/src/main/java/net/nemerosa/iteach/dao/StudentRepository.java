package net.nemerosa.iteach.dao;

public interface StudentRepository {

    int create(int teacherId, int schoolId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email);

}
