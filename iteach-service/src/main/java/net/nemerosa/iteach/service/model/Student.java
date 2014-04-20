package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

@Data
public class Student {

    private final int id;
    private final int teacherId;
    private final int schoolId;
    private final Integer contractId;
    private final boolean disabled;
    private final String name;
    private final String subject;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;

}
