package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TInvoice;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface InvoiceRepository {

    int create(int teacherId, int schoolId, int year, Month month, long number, String type, LocalDateTime generation);

    List<TInvoice> list(int teacherId, Integer schoolId, Integer year);

    void download(int teacherId, int invoiceId, OutputStream out);

    Long getLastInvoiceNumber(int teacherId);

    void downloaded(int teacherId, int invoiceId);

    void startGeneration(int teacherId, int invoiceId);

    void save(int teacherId, int id, byte[] document);

    TInvoice getById(int teacherId, int invoiceId);
}
