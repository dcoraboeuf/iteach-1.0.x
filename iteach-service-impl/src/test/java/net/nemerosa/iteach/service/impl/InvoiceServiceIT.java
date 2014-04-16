package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.InvoiceStatus;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.InvoiceService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.invoice.InvoiceFixtures;
import net.nemerosa.iteach.service.model.*;
import org.apache.commons.io.FileUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Locale;

import static org.junit.Assert.*;

public class InvoiceServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AccountService accountService;

    @Test(timeout = 30 * 1000L) // Max 30s
    public void generate() throws Exception {
        // Gets a teacher
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        // Profile for the teacher
        serviceITSupport.asTeacher(teacherId, () -> {
            accountService.saveProfile(new Profile(
                    "My Company",
                    "Company Address",
                    "01234",
                    "BE1234",
                    "BE56575757",
                    "RTTRRTRT"
            ));
            accountService.updateProfileCompanyLogo(InvoiceFixtures.companyLogo());
            return null;
        });

        // Creates a school
        int schoolId = serviceITSupport.createSchool(teacherId);
        School school = serviceITSupport.asTeacher(teacherId, () -> teacherService.getSchool(schoolId));
        assertEquals(BigDecimal.valueOf(21), school.getVatRate());
        assertEquals(Money.of(CurrencyUnit.EUR, 56), school.getHourlyRate());

        // Creates two students for this school
        Student student1 = serviceITSupport.createStudent(teacherId, schoolId);
        Student student2 = serviceITSupport.createStudent(teacherId, schoolId);

        // Creates a few lesson for student 1 in April 2014 (4 hours in total)
        serviceITSupport.asTeacher(teacherId, () -> {
            teacherService.createLesson(new LessonForm(student1.getId(), "S1L1", LocalDateTime.of(2014, 4, 2, 10, 0), LocalDateTime.of(2014, 4, 2, 12, 0)));
            teacherService.createLesson(new LessonForm(student1.getId(), "S1L2", LocalDateTime.of(2014, 4, 7, 10, 0), LocalDateTime.of(2014, 4, 7, 12, 0)));
            return null;
        });

        // Creates a few lesson for student 2 in April 2014 (6 hours in total)
        serviceITSupport.asTeacher(teacherId, () -> {
            teacherService.createLesson(new LessonForm(student2.getId(), "S2L1", LocalDateTime.of(2014, 4, 2, 14, 0), LocalDateTime.of(2014, 4, 2, 16, 0)));
            teacherService.createLesson(new LessonForm(student2.getId(), "S2L2", LocalDateTime.of(2014, 4, 7, 14, 0), LocalDateTime.of(2014, 4, 7, 16, 0)));
            teacherService.createLesson(new LessonForm(student2.getId(), "S2L3", LocalDateTime.of(2014, 4, 7, 14, 0), LocalDateTime.of(2014, 4, 7, 16, 0)));
            return null;
        });

        // Generates an invoice for April 2014 for this school, with invoice number = 2014007
        InvoiceInfo info = serviceITSupport.asTeacher(teacherId, () -> invoiceService.generate(
                new InvoiceForm(
                        schoolId,
                        YearMonth.of(2014, 4),
                        2014007,
                        true,
                        "A comment"
                ),
                "application/pdf",
                Locale.ENGLISH
        ));

        // Checks the initial invoice info
        assertNotNull(info);
        int invoiceId = info.getId();
        assertEquals("Status of the invoice must be CREATED", InvoiceStatus.CREATED, info.getStatus());
        assertTrue("ID of the invoice must be > 0", invoiceId > 0);
        assertEquals("application/pdf", info.getDocumentType());
        assertEquals(schoolId, info.getSchoolId());
        assertEquals(2014007, info.getNumber());

        // Waits until the invoice is actually generated
        while (!info.getStatus().isFinished()) {
            // Gets the next status
            info = serviceITSupport.asTeacher(teacherId, () -> invoiceService.getInvoiceInfo(invoiceId));
            // Waits a bit
            Thread.sleep(500);
        }

        // Invoice generation is now finished.
        // Checks it is finished
        assertEquals(InvoiceStatus.READY, info.getStatus());
        assertFalse("Invoice must not be marked as downloaded", info.isDownloaded());

        // Downloads the invoice document
        Document doc = serviceITSupport.asTeacher(teacherId, () -> invoiceService.downloadInvoice(invoiceId));
        assertEquals(String.format("%s-201404-2014007", school.getName()), doc.getTitle());
        assertEquals("application/pdf", doc.getType());
        assertTrue("The document must not be empty", doc.getContent().length > 0);

        // Saves in a file
        FileUtils.writeByteArrayToFile(
                new File(new File("target/invoice"), doc.getTitle() + "." + doc.getExtension()),
                doc.getContent()
        );

        // Marks the invoice as being downloaded
        serviceITSupport.asTeacher(teacherId, () -> {
            invoiceService.invoiceIsDownloaded(invoiceId);
            return null;
        });

        // Checks the downloaded status
        info = serviceITSupport.asTeacher(teacherId, () -> invoiceService.getInvoiceInfo(invoiceId));
        assertTrue("Invoice must be marked as downloaded", info.isDownloaded());
    }

}
