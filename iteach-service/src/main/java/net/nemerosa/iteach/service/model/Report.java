package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Report {

    private final BigDecimal hours;
    private final Money income;
    private final List<SchoolReport> schools;

}
