package net.nemerosa.iteach.service.model;

import lombok.Data;
import net.nemerosa.iteach.common.InvoiceStatus;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class InvoiceInfo {

    private final int id;
    private final InvoiceStatus status;
    private final String errorMessage;
    private final String errorUuid;
    private final int schoolId;
    private final YearMonth period;
    private final long number;
    private final LocalDateTime generation;
    private final boolean downloaded;
    private final String documentType;

}
