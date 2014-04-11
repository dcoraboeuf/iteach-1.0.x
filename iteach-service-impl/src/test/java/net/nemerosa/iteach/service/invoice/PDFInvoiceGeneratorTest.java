package net.nemerosa.iteach.service.invoice;

import net.nemerosa.iteach.service.model.InvoiceData;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class PDFInvoiceGeneratorTest {

    @Test
    public void hours_format() {
        assertEquals("2.5", PDFInvoiceGenerator.formatHours(BigDecimal.valueOf(2.5), Locale.ENGLISH));
        assertEquals("2", PDFInvoiceGenerator.formatHours(BigDecimal.valueOf(2.0), Locale.ENGLISH));
        assertEquals("2,5", PDFInvoiceGenerator.formatHours(BigDecimal.valueOf(2.5), Locale.FRENCH));
        assertEquals("2", PDFInvoiceGenerator.formatHours(BigDecimal.valueOf(2.0), Locale.FRENCH));
    }

    @Test
    public void generate() throws IOException {
        // Sample data
        InvoiceData data = InvoiceFixtures.invoiceData();
        // Generation
        PDFInvoiceGenerator generator = new PDFInvoiceGenerator();
        byte[] bytes = generator.generate(data, Locale.ENGLISH);
        // Saves into a file
        FileUtils.writeByteArrayToFile(
                new File("target/test.pdf"),
                bytes
        );
    }

}
