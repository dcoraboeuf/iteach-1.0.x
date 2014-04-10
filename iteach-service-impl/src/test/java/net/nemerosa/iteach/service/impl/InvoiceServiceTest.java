package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.InvoiceService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvoiceServiceTest {

    private InvoiceService service;
    private InvoiceRepository invoiceRepository;
    private SecurityUtils securityUtils;

    @Before
    public void before() {
        TeacherService teacherService = mock(TeacherService.class);
        AccountService accountService = mock(AccountService.class);
        invoiceRepository = mock(InvoiceRepository.class);
        securityUtils = mock(SecurityUtils.class);
        service = new InvoiceServiceImpl(
                teacherService,
                accountService,
                Collections.emptyList(),
                invoiceRepository,
                securityUtils,
                null,
                null);
    }

    @Test
    public void next_version_number_when_none_before() {
        when(invoiceRepository.getLastInvoiceNumber(1)).thenReturn(null);
        when(securityUtils.checkTeacher()).thenReturn(1);
        assertEquals(1L, service.getNextInvoiceNumber());
    }

    @Test
    public void next_version_number_when_one_before() {
        when(invoiceRepository.getLastInvoiceNumber(1)).thenReturn(10L);
        when(securityUtils.checkTeacher()).thenReturn(1);
        assertEquals(11L, service.getNextInvoiceNumber());
    }

}
