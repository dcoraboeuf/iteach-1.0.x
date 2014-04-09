package net.nemerosa.iteach.service.invoice;

import net.nemerosa.iteach.service.model.InvoiceData;

import java.util.Locale;

public interface InvoiceGenerator {

    /**
     * MIME type this generator can generate.
     */
    String getType();

    /**
     * Generates the document for this invoice's data.
     */
    byte[] generate(InvoiceData data, Locale locale);
}
