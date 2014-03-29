package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.time.YearMonth;

/**
 * Data needed to create an invoice
 */
@Data
public class InvoiceForm {

    private final int schoolId;
    private final YearMonth period;
    private final long number;

}
