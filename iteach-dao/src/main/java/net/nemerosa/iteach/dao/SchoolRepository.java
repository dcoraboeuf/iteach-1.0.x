package net.nemerosa.iteach.dao;

import org.joda.money.Money;

public interface SchoolRepository {

    int create(int teacherId, String name, String contact, String colour, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite);

}
