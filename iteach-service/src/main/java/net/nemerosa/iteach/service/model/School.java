package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

@Data
public class School {

    private final int id;
    private final int teacherId;
    private final String name;
    private final String colour;
    private final String contact;
    private final Money hourlyRate;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final String webSite;

}
