package net.nemerosa.iteach.ui.model;

import lombok.Data;

/**
 * Data used to initialize the creation of an invoice request, typically the next invoice number to use.
 */
@Data
public class UIInvoiceFormData {

    private final long nextInvoiceNumber;
    private final boolean detailPerStudent;

}
