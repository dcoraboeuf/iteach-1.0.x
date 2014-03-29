package net.nemerosa.iteach.ui.model;

import lombok.Data;

@Data
public class UIInvoiceData {

    private final long number;
    private final String teacherName;
    private final String teacherEmail;
    private final UIProfile profile;
    private final UISchool school;
    private final UISchoolReport report;

}
