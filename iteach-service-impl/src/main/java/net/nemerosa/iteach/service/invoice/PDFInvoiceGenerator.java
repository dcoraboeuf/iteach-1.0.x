package net.nemerosa.iteach.service.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import net.nemerosa.iteach.service.InvoiceGenerationException;
import net.nemerosa.iteach.service.model.InvoiceData;
import net.nemerosa.iteach.service.model.StudentReport;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

            // TODO Company logo
            document.add(header(data));
            document.add(getInvoicePara(data, locale, tab1));
            document.add(studentDetail(data, locale));

            document.add(total(data, locale));

            // TODO Coordinates

            // End of the document
            document.close();

            // OK
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new InvoiceGenerationException(e);
        }
    }

    private Paragraph total(InvoiceData data, Locale locale) {
        Paragraph p = new Paragraph();
        p.add(new Paragraph("Total", section));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Line 1
        table.addCell(cell("Total hours:"));
        table.addCell(cell(formatHours(data.getReport().getHours(), locale) + " hours"));
        table.addCell(cell("x")); // TODO Unicode for x
        table.addCell(cell(data.getSchool().getHourlyRate().toString()));
        table.addCell(cell(data.getReport().getIncome().toString(), Element.ALIGN_RIGHT));

        // Line 2
        filler(table, 2);
        PdfPCell vatRate = cell(String.format(locale, "VAT %s%%", data.getSchool().getVatRate()), Element.ALIGN_RIGHT);
        vatRate.setColspan(2);
        table.addCell(vatRate);
        table.addCell(cell(data.getVat().toString(), Element.ALIGN_RIGHT));

        // Line 3
        filler(table, 2);
        PdfPCell vatTotal = cell("Total with VAT", Element.ALIGN_RIGHT);
        vatTotal.setColspan(2);
        table.addCell(vatTotal);
        table.addCell(cell(data.getVatTotal().toString(), Element.ALIGN_RIGHT));

        p.add(table);
        return p;
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

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        for (StudentReport student : data.getReport().getStudents()) {
            table.addCell(cell(student.getName()));
            PdfPCell cell = cell(formatHours(student.getHours(), locale) + " hours");
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
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

    private PdfPTable header(InvoiceData data) {
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
        // OK for the table
        return table;
    }

    private PdfPCell cell(String text) {
        return cell(text, Element.ALIGN_LEFT);
    }

    private PdfPCell cell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(0);
        cell.setHorizontalAlignment(alignment);
        return cell;
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
