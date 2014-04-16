package net.nemerosa.iteach.dao.model;

import lombok.Data;

@Data
public class TProfile {

    private final String company;
    private final String postalAddress;
    private final String phone;
    private final String vat;
    private final String iban;
    private final String bic;

}
