package net.nemerosa.iteach.service.model;

import lombok.Data;

/**
 * Invoice data for a school and a month.
 */
@Data
public class InvoiceData {

    /**
     * Invoice number
     */
    private final long number;
    /**
     * Data for the teacher
     */
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

}
