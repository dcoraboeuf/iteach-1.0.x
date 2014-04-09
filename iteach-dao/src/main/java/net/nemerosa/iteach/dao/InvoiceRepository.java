package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TInvoice;

import java.io.OutputStream;
import java.time.Month;
import java.util.List;

public interface InvoiceRepository {

    int save(int teacherId, int schoolId, int year, Month month, long number, String type, byte[] document);

    List<TInvoice> list(int teacherId, Integer schoolId, Integer year);

    void download(int teacherId, int invoiceId, OutputStream out);

    Long getLastInvoiceNumber(int teacherId);

    void downloaded(int teacherId, int invoiceId);
}
