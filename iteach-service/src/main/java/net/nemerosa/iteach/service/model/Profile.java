package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class Profile {

    private final String company;
    private final String postalAddress;
    private final String phone;
    private final String vat;
    private final String iban;
    private final String bic;

}
