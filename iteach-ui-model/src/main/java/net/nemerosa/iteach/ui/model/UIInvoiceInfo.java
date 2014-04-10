package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.InvoiceStatus;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class UIInvoiceInfo extends UIResource {

    private final int id;
    private final InvoiceStatus status;
    private final String errorMessage;
    private final String errorUuid;
    private final String href;
    private final UISchoolSummary school;
    private final YearMonth period;
    private final long number;
    private final LocalDateTime generation;
    private final boolean downloaded;
    private final String documentType;

    @ConstructorProperties({"id", "status", "errorMessage", "errorUuid", "school", "period", "number", "generation", "downloaded", "documentType"})
    public UIInvoiceInfo(int id, InvoiceStatus status, String errorMessage, String errorUuid, UISchoolSummary school, YearMonth period, long number, LocalDateTime generation, boolean downloaded, String documentType) {
        this.id = id;
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorUuid = errorUuid;
        this.school = school;
        this.period = period;
        this.number = number;
        this.generation = generation;
        this.downloaded = downloaded;
        this.documentType = documentType;
        this.href = UILink.href("api/teacher/invoice/%d", id);
    }
}
