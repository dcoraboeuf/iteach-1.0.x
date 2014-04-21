package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class StudentForm {

    private final int schoolId;
    private final Integer contractId;
    private final String name;
    private final String subject;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;

}
