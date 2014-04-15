package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.PreferencesService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreferencesServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private PreferencesService preferencesService;

    private int teacherId;

    @Before
    public void before() {
        teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
    }

    @Test
    public void getInvoiceStudentDetail() throws Exception {
        serviceITSupport.asTeacher(teacherId, () -> {
            assertFalse("False by default", preferencesService.getInvoiceStudentDetail());
            preferencesService.setInvoiceStudentDetail(true);
            assertTrue("Set to true", preferencesService.getInvoiceStudentDetail());
            return null;
        });
    }
}
