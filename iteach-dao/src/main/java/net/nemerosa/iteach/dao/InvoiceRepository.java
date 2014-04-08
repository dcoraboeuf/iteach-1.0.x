package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TInvoice;

import java.time.Month;
import java.util.List;

public interface InvoiceRepository {

    int save(int teacherId, int schoolId, int year, Month month, long number, String type, byte[] document);

    List<TInvoice> list(int teacherId, Integer schoolId, Integer year);

}
