package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.InvoiceForm;
import net.nemerosa.iteach.service.model.InvoiceInfo;

import java.io.OutputStream;
import java.util.List;
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

    /**
     * Gets the list of invoices.
     *
     * @param school Optional school ID used to filter
     * @param year   Optional year used to filter
     */
    List<InvoiceInfo> getInvoices(Integer school, Integer year);

    /**
     * Downloads an invoice using its ID
     */
    void downloadInvoice(int invoiceId, OutputStream out);

    /**
     * Gets the next invoice number
     */
    long getNextInvoiceNumber();

}
