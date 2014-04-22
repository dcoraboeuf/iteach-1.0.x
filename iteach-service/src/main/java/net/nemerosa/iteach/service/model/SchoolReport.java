package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SchoolReport {

    private final int id;
    private final String name;
    private final String colour;
    private final BigDecimal hours;
    private final Money income;
    private final Money incomeTotal;
    private final List<ContractReport> contracts;

}
