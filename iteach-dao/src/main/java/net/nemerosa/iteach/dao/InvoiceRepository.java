package net.nemerosa.iteach.dao;

import java.time.Month;

public interface InvoiceRepository {

    int save(int teacherId, int schoolId, int year, Month month, long number, String type, byte[] document);

}
