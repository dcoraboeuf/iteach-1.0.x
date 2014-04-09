package net.nemerosa.iteach.service.invoice;

import net.nemerosa.iteach.service.model.InvoiceData;
import net.nemerosa.iteach.service.model.Profile;
import net.nemerosa.iteach.service.model.School;
import org.apache.commons.io.FileUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

public class PDFInvoiceGeneratorTest {

    @Test
    public void generate() throws IOException {
        // Sample data
        InvoiceData data = new InvoiceData(
                YearMonth.of(2014, 4),
                LocalDate.now(),
                2014001L,
                1, "Teacher Name", "teacher@test.com",
                new Profile("Company", "", "Rue de la Ville\n1000 Brussels", "01234", "56789", "BE567", "BBGGRR", 1L),
                new School(1, 1, "School", "#FF0000", "", Money.of(CurrencyUnit.EUR, 56), "Avenue du Village\n3000 Labas", "", "", "", "", "", BigDecimal.valueOf(21)),
                null,
                null,
                null
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
