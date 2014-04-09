package net.nemerosa.iteach.service.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import net.nemerosa.iteach.service.InvoiceGenerationException;
import net.nemerosa.iteach.service.model.InvoiceData;
import net.nemerosa.iteach.service.model.StudentReport;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static com.itextpdf.text.Element.ALIGN_RIGHT;

@Component
public class PDFInvoiceGenerator implements InvoiceGenerator {

    private static final Font section = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font amount = new Font(Font.FontFamily.COURIER, 14, Font.NORMAL);
    private static final Font amountTotal = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
    public static final int TABLE_WIDTH = 75;
    public static final int AMOUNT_BORDER_WIDTH = 2;
    public static final int PADDING = 5;

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

            // TODO Company logo
            document.add(header(data));
            document.add(getInvoicePara(data, locale, tab1));
            document.add(studentDetail(data, locale));
            document.add(total(data, locale));
            document.add(coordinates(data));

            // TODO Message about the IBAN & BIC

            // End of the document
            document.close();

            // OK
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new InvoiceGenerationException(e);
        }
    }

    private Element coordinates(InvoiceData data) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        // Line 1
        table.addCell(cell().withText(data.getProfile().getCompany()).done());
        table.addCell(cell().withText("Tel:").withAlign(ALIGN_RIGHT).done());
        table.addCell(cell().withText(data.getProfile().getPhone()).withAlign(ALIGN_RIGHT).done());

        // Line 2
        table.addCell(cell().withText(data.getProfile().getPostalAddress()).withRowspan(2).done());
        table.addCell(cell().withText("Email:").withAlign(ALIGN_RIGHT).done());
        table.addCell(cell().withText(data.getTeacherEmail()).withAlign(ALIGN_RIGHT).done());

        // Line 3
        table.addCell(cell().withText("VAT:").withAlign(ALIGN_RIGHT).done());
        table.addCell(cell().withText(data.getProfile().getVat()).withAlign(ALIGN_RIGHT).done());

        // Line 4
        table.addCell(cell().withText("IBAN:").withAlign(ALIGN_RIGHT).withColspan(2).done());
        table.addCell(cell().withText(data.getProfile().getIban()).withAlign(ALIGN_RIGHT).done());

        // Line 5
        table.addCell(cell().withText("BIC:").withAlign(ALIGN_RIGHT).withColspan(2).done());
        table.addCell(cell().withText(data.getProfile().getBic()).withAlign(ALIGN_RIGHT).done());

        // Container
        PdfPTable section = new PdfPTable(1);
        section.setSpacingBefore(50);
        section.setWidthPercentage(100);
        section.addCell(table);

        // OK for the table
        return section;
    }

    private Paragraph total(InvoiceData data, Locale locale) {
        Paragraph p = new Paragraph();
        p.add(new Paragraph("Total", section));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(TABLE_WIDTH);

        // Line 1
        table.addCell(cell().withText("Total hours:").withPadding(PADDING).done());
        table.addCell(
                cell()
                        .withText(
                                formatHours(data.getReport().getHours(), locale) + " hours"
                                        + " \u00D7 "
                                        + data.getSchool().getHourlyRate().toString()
                        )
                        .withAlign(ALIGN_RIGHT)
                        .withPadding(PADDING)
                        .done()
        );
        table.addCell(amount(data.getReport().getIncome()).done());

        // Line 2
        filler(table, 1);
        table.addCell(
                cell()
                        .withText(String.format(locale, "VAT %s%%", data.getSchool().getVatRate()))
                        .withAlign(ALIGN_RIGHT)
                        .withPadding(PADDING)
                        .done()
        );
        table.addCell(amount(data.getVat()).done());

        // Line 3
        filler(table, 1);
        table.addCell(
                cell()
                        .withText("Total with VAT")
                        .withAlign(ALIGN_RIGHT)
                        .withPadding(PADDING)
                        .done()
        );
        table.addCell(
                amount(data.getVatTotal())
                        .withFont(amountTotal)
                        .withBorderWidth(AMOUNT_BORDER_WIDTH)
                        .done()
        );

        p.add(table);
        return p;
    }

    private CellBuilder amount(Money amount) {
        return cell()
                .withText(amount.toString())
                .withFont(PDFInvoiceGenerator.amount)
                .withAlign(ALIGN_RIGHT)
                .withPadding(PADDING);
    }

    private CellBuilder cell() {
        return CellBuilder.create();
    }

    private void filler(PdfPTable table, int colspan) {
        PdfPCell filler = cell("");
        filler.setColspan(colspan);
        table.addCell(filler);
    }

    private Paragraph studentDetail(InvoiceData data, Locale locale) {
        // Details of students
        // TODO Details of student is optional
        Paragraph p = new Paragraph();
        p.add(new Paragraph("Detail per student", section));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(TABLE_WIDTH);
        for (StudentReport student : data.getReport().getStudents()) {
            table.addCell(
                    cell()
                            .withText(student.getName())
                            .withPadding(PADDING)
                            .done()
            );
            table.addCell(
                    cell()
                            .withText(formatHours(student.getHours(), locale) + " hours")
                            .withAlign(ALIGN_RIGHT)
                            .withPadding(PADDING)
                            .done()
            );
            filler(table, 1);
        }
        p.add(table);
        return p;
    }

    public static String formatHours(BigDecimal hours, Locale locale) {
        NumberFormat format = DecimalFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(1);
        return format.format(hours);
    }

    private Element header(InvoiceData data) {
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
        // VAT
        table.addCell(cell("VAT: " + data.getProfile().getVat()));
        table.addCell(cell("VAT: " + data.getSchool().getVat()));

        PdfPTable section = new PdfPTable(1);
        section.setWidthPercentage(100);
        section.addCell(table);

        // OK for the table
        return section;
    }

    private PdfPCell cell(String text) {
        return cell(text, Element.ALIGN_LEFT);
    }

    private PdfPCell cell(String text, int alignment) {
        return cell().withText(text).withAlign(alignment).done();
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
        line.add(new Chunk(label));
        line.add(tab1);
        line.add(new Chunk(value));
        return line;
    }
}
