package net.nemerosa.iteach.dao.model;

import lombok.Data;
import net.nemerosa.iteach.common.InvoiceStatus;

import java.time.LocalDateTime;

@Data
public class TInvoice {

    private final int id;
    private final InvoiceStatus status;
    private final int school;
    private final int year;
    private final int month;
    private final LocalDateTime generation;
    private final long number;
    private final boolean downloaded;
    private final String documentType;

}
