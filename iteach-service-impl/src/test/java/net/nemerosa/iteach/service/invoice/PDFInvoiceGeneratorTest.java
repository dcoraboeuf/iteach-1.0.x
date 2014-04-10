package net.nemerosa.iteach.service.invoice;

import net.nemerosa.iteach.service.model.*;
import org.apache.commons.io.FileUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
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
        final Money hourlyRate = Money.of(CurrencyUnit.EUR, 20);
        final int teacherId = 1;
        final int schoolId = 1;
        final String schoolColour = "#FF0000";
        final String schoolName = "School";
        InvoiceData data = new InvoiceData(
                YearMonth.of(2014, 4),
                LocalDate.now(),
                2014001L,
                teacherId, "Teacher Name", "teacher@test.com",
                new Profile("Company", "", "Rue de la Ville\n1000 Brussels", "01234", "BE56789", "BE567", "BBGGRR"),
                new School(schoolId, teacherId, schoolName, schoolColour, "", hourlyRate, "Avenue du Village\n3000 Labas", "", "", "", "", "BE09876", BigDecimal.valueOf(21)),
                new SchoolReport(
                        schoolId,
                        schoolName,
                        schoolColour,
                        hourlyRate,
                        BigDecimal.valueOf(10),
                        Money.of(CurrencyUnit.EUR, 200),
                        Arrays.asList(
                                new StudentReport(
                                        1, false, "Student 1", "Subject 1",
                                        BigDecimal.valueOf(6.5),
                                        Money.of(CurrencyUnit.EUR, 120)
                                ),
                                new StudentReport(
                                        2, false, "Student 2", "Subject 2",
                                        BigDecimal.valueOf(3.5),
                                        Money.of(CurrencyUnit.EUR, 80)
                                )
                        )
                ),
                Money.of(CurrencyUnit.EUR, 42.0),
                Money.of(CurrencyUnit.EUR, 242.0)
        );
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
