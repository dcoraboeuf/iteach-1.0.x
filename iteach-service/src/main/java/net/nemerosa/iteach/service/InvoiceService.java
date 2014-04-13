package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.service.model.InvoiceFilter;
import net.nemerosa.iteach.service.model.InvoiceForm;
import net.nemerosa.iteach.service.model.InvoiceInfo;

import java.util.List;
import java.util.Locale;

public interface InvoiceService {

    /**
     * Generates an invoice. The actual generation of the invoice is launched asynchronously. The return invoice
     * info contains an ID that can be used to get the information about this invoice.
     *
     * @param invoiceForm Data used for the generation.
     * @param type        Document type
     * @param locale      Locale used for the generation of the invoice
     * @return State of the invoice (typically {@link net.nemerosa.iteach.common.InvoiceStatus#CREATED}).
     * @see #getInvoiceInfo(int)
     */
    InvoiceInfo generate(InvoiceForm invoiceForm, String type, Locale locale);

    /**
     * Gets information about a given invoice.
     */
    InvoiceInfo getInvoiceInfo(int id);

    /**
     * Gets the list of invoices.
     */
    List<InvoiceInfo> getInvoices(InvoiceFilter filter);

    /**
     * Downloads an invoice using its ID
     */
    Document downloadInvoice(int invoiceId);

    /**
     * Notifies the download is OK
     */
    void invoiceIsDownloaded(int invoiceId);

    /**
     * Gets the next invoice number
     */
    long getNextInvoiceNumber();

    /**
     * Deletes a list of invoices
     */
    Ack deleteInvoices(List<Integer> ids);

    /**
     * Gets the total number of invoices
     */
    int getTotalCount();
}
