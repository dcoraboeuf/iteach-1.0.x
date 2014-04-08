package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class InvoiceInfo {

    private final int id;
    private final int schoolId;
    private final YearMonth period;
    private final long number;
    private final LocalDateTime generation;
    private final String documentType;

}
