package net.nemerosa.iteach.service.model;

import lombok.Data;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Invoice data for a school and a month.
 */
@Data
public class InvoiceData {

    /**
     * Period for the invoice
     */
    private final YearMonth period;
    private final LocalDate date;
    /**
     * Invoice number
     */
    private final long number;
    /**
     * Data for the teacher
     */
    private final int teacherId;
    private final String teacherName;
    private final String teacherEmail;
    /**
     * Profile used for the invoice
     */
    private final Profile profile;
    /**
     * School data
     */
    private final School school;
    /**
     * School report
     */
    private final SchoolReport report;
    /**
     * VAT support
     */
    private final Money vat;
    private final Money vatTotal;
    /**
     * Optional comment
     */
    private final String comment;
    /**
     * Detail per student
     */
    private final boolean detailPerStudent;

}
