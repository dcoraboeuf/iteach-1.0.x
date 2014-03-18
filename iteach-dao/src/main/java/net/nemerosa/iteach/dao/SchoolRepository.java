package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TSchool;
import org.joda.money.Money;

import java.util.List;

public interface SchoolRepository {

    int create(int teacherId, String name, String colour, String contact, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite);

    TSchool getById(int teacherId, int schoolId);

    List<TSchool> findAll(int teacherId);

    void update(int teacherId, int schoolId, String name, String colour, String contact, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite);
}
