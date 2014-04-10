package net.nemerosa.iteach.ui.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Data used to launch the generation of an invoice.
 */
@Data
public class UIInvoiceForm {

    @Min(1)
    private final int schoolId;

    @Min(2014)
    private final int year;

    @Min(1)
    @Max(12)
    private final int month;

    @Min(1)
    private final long number;

}
