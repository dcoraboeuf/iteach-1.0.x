package net.nemerosa.iteach.service.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import net.nemerosa.iteach.service.InvoiceGenerationException;
import net.nemerosa.iteach.service.model.InvoiceData;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Component
public class PDFInvoiceGenerator implements InvoiceGenerator {

    private static final Font section = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

    @Override
    public String getType() {
        return "application/pdf";
    }

    @Override
    public byte[] generate(InvoiceData data) {
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

            Paragraph p = new Paragraph();
            p.add(new Paragraph("Invoice", section));
            p.add(new Paragraph(""));

            p.add(tabbedLine(tab1, "Invoice number:", String.valueOf(data.getNumber())));

            document.add(p);

            // End of the document
            document.close();

            // OK
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new InvoiceGenerationException(e);
        }
    }

    private Paragraph tabbedLine(Chunk tab1, String label, String value) {
        Paragraph line = new Paragraph();
        line.add(new Chunk(label, normal));
        line.add(tab1);
        line.add(new Chunk(value, normal));
        return line;
    }
}
