package net.nemerosa.iteach.service.io.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

@Data
public class XSchool {
    private final String name;
    private final String colour;
    private final String contact;
    private final Money hourlyRate;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final String webSite;
    private final String vat;
    private final BigDecimal vatRate;
    private final List<XContract> contracts;
    private final List<XStudent> students;
    private final List<XComment> comments;
}
