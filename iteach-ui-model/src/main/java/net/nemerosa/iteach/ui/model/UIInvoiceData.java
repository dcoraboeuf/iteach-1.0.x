package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class UIInvoiceData {

    private final YearMonth period;
    private final LocalDate date;
    private final String formattedPeriod;
    private final String formattedDate;
    private final long number;
    private final String teacherName;
    private final String teacherEmail;
    private final UIProfile profile;
    private final UISchool school;
    private final UISchoolReport report;

}
