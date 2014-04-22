package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class StudentReport {
    private final int id;
    private final boolean disabled;
    private final String name;
    private final String subject;
    private final BigDecimal hours;
    private final Contract contract;
    private final Money hourlyRate;
    private final Money income;
}
