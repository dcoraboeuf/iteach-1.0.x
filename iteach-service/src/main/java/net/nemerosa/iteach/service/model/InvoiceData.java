package net.nemerosa.iteach.service.model;

import lombok.Data;
import net.nemerosa.iteach.common.UntitledDocument;
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
     * Logo of the company
     */
    private final UntitledDocument companyLogo;
    /**
     * School data
     */
    private final School school;
    /**
     * School report
     */
    private final SchoolReport report;
    /**
     * Title of the invoice
     */
    private final String title;
    /**
     * Optional comment
     */
    private final String comment;
    /**
     * Detail per student
     */
    private final boolean detailPerStudent;

}
