package net.nemerosa.iteach.service.io.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class XContract {
    private final int refId;
    private final String name;
    private final Money hourlyRate;
    private final BigDecimal vatRate;
}
