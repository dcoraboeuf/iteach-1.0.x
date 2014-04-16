package net.nemerosa.iteach.service.invoice;

import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.service.model.*;
import org.apache.commons.io.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

public final class InvoiceFixtures {

    private InvoiceFixtures() {
    }

    public static InvoiceData invoiceData() {
        final Money hourlyRate = Money.of(CurrencyUnit.EUR, 20);
        final int teacherId = 1;
        final int schoolId = 1;
        final String schoolColour = "#FF0000";
        final String schoolName = "School";
        return new InvoiceData(
                YearMonth.of(2014, 4),
                LocalDate.now(),
                2014001L,
                teacherId, "Teacher Name", "teacher@test.com",
                new Profile("Company", "Rue de la Ville\n1000 Brussels", "01234", "BE56789", "BE567", "BBGGRR"),
                companyLogo(),
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
                Money.of(CurrencyUnit.EUR, 242.0),
                "Some comments",
                true
        );
    }

    public static UntitledDocument companyLogo() {
        try (InputStream in = InvoiceFixtures.class.getResourceAsStream("/logo.png")) {
            return new UntitledDocument(
                    "image/png",
                    IOUtils.toByteArray(in)
            );
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read image", e);
        }
    }

    public static InvoiceData invoiceIncompleteData() {
        final int teacherId = 1;
        final int schoolId = 1;
        final String schoolColour = "#FF0000";
        final String schoolName = "School";
        return new InvoiceData(
                YearMonth.of(2014, 4),
                LocalDate.now(),
                2014001L,
                teacherId, "Teacher Name", "teacher@test.com",
                new Profile("", "", "", "", "", ""),
                null,
                new School(schoolId, teacherId, schoolName, schoolColour, "", null, "", "", "", "", "", "", null),
                new SchoolReport(
                        schoolId,
                        schoolName,
                        schoolColour,
                        null,
                        BigDecimal.ZERO,
                        Money.of(CurrencyUnit.EUR, 0),
                        Arrays.asList(
                        )
                ),
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                "",
                false
        );
    }
}
