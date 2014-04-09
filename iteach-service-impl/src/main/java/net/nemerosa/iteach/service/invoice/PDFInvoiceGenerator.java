package net.nemerosa.iteach.service.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import net.nemerosa.iteach.service.InvoiceGenerationException;
import net.nemerosa.iteach.service.model.InvoiceData;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class PDFInvoiceGenerator implements InvoiceGenerator {

    private static final Font section = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

    @Override
    public String getType() {
        return "application/pdf";
    }

    @Override
    public byte[] generate(InvoiceData data, Locale locale) {
        // TODO Using the locale for the generation
        locale = Locale.ENGLISH;
        // Creates the output
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Creates the document
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            // Opens the document
            document.open();
            // Meta-information
            document.addAuthor(data.getTeacherName());
            document.addCreator("iTeach");
            document.addTitle(String.format("Invoice")); // TODO Complete title

            @SuppressWarnings("deprecation")
            Chunk tab1 = new Chunk(new VerticalPositionMark(), 150, false);


            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            // From/To
            table.addCell(cell("From:"));
            table.addCell(cell("To:"));
            // Company -- School name
            table.addCell(cell(data.getProfile().getCompany()));
            table.addCell(cell(data.getSchool().getName()));
            // Addresses
            table.addCell(cell(data.getProfile().getPostalAddress()));
            table.addCell(cell(data.getSchool().getPostalAddress()));
            // OK for the table
            document.add(table);

            document.add(getInvoicePara(data, locale, tab1));

            // End of the document
            document.close();

            // OK
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new InvoiceGenerationException(e);
        }
    }

    private PdfPCell cell(String text) {
        PdfPCell from = new PdfPCell(new Phrase(text));
        from.setBorder(0);
        return from;
    }

    private Paragraph getInvoicePara(InvoiceData data, Locale locale, Chunk tab1) {
        Paragraph p = new Paragraph();
        p.add(new Paragraph("Invoice", section));
        p.add(new Paragraph(""));
        p.add(tabbedLine(tab1, "Invoice number:", String.valueOf(data.getNumber())));
        p.add(tabbedLine(tab1, "Invoice date:", DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale).format(data.getDate())));
        p.add(tabbedLine(tab1, "Work carried out by:", data.getTeacherName()));
        p.add(tabbedLine(tab1, "Period:", DateTimeFormatter.ofPattern("MMMM yyyy", locale).format(data.getPeriod())));
        return p;
    }

    private Paragraph tabbedLine(Chunk tab1, String label, String value) {
        Paragraph line = new Paragraph();
        line.add(new Chunk(label, normal));
        line.add(tab1);
        line.add(new Chunk(value, normal));
        return line;
    }
}
