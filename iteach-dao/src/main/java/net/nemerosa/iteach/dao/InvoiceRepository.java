package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.InvoiceStatus;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.dao.model.TInvoice;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository {

    int create(int teacherId, int schoolId, int year, int month, long number, String type, LocalDateTime generation);

    UntitledDocument download(int teacherId, int invoiceId);

    Long getLastInvoiceNumber(int teacherId);

    void downloaded(int teacherId, int invoiceId);

    void startGeneration(int teacherId, int invoiceId);

    void save(int teacherId, int id, byte[] document);

    TInvoice getById(int teacherId, int invoiceId);

    void error(int teacherId, int invoiceId, String message, String uuid);

    void delete(int teacherId, int invoiceId);

    List<TInvoice> list(int teacherId, Integer schoolId, Integer year, Boolean downloaded, InvoiceStatus status, int pageOffset, int pageSize);

    int totalCount(int teacherId);
}
