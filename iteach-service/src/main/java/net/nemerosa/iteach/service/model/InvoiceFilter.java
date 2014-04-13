package net.nemerosa.iteach.service.model;

import lombok.Data;
import net.nemerosa.iteach.common.InvoiceStatus;

@Data
public class InvoiceFilter {

    private final Integer schoolId;
    private final Integer year;
    private final Boolean downloaded;
    private final InvoiceStatus status;
    private final int pageOffset;
    private final int pageSize;

}
