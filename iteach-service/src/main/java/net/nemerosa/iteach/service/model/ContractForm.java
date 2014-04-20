package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class ContractForm {
    private final String name;
    private final Money hourlyRate;
    private final BigDecimal vatRate;
}
