package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.InvoiceForm;

import java.util.concurrent.Future;

public interface InvoiceService {

    /**
     * Generates an invoice.
     *
     * @param invoiceForm Data used for the generation.
     * @param type        Document type
     * @return ID of the generated invoice when complete.
     */
    Future<Integer> generate(InvoiceForm invoiceForm, String type);

    // TODO Gets info about an invoice using its ID
    // TODO Downloads an invoice using its ID

}
