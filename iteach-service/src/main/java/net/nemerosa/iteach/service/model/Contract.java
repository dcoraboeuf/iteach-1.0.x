package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class Contract {

    private final int id;
    private final int schoolId;
    private final String name;
    private final Money hourlyRate;
    private final BigDecimal vatRate;

}
