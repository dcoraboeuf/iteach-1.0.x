package net.nemerosa.iteach.dao.model;

import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class TContract {
    private final int id;
    private final int teacherId;
    private final int schoolId;
    private final String name;
    private final Money hourlyRate;
    private final BigDecimal vatRate;
}
