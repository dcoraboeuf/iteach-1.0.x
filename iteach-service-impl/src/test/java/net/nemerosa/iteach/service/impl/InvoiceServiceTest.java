package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.invoice.InvoiceFixtures;
import net.nemerosa.iteach.service.model.InvoiceData;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvoiceServiceTest {

    private InvoiceServiceImpl service;
    private InvoiceRepository invoiceRepository;
    private SecurityUtils securityUtils;
    private final Strings strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH);

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

    @Test
    public void control_ok() {
        List<Localizable> messages = new ArrayList<>();
        service.control(messages, true, "any");
        assertTrue("No message was added", messages.isEmpty());
    }

    @Test
    public void control_nok() {
        List<Localizable> messages = new ArrayList<>();
        service.control(messages, false, "any");
        assertEquals("One message was added", 1, messages.size());
        assertTrue(messages.get(0) instanceof LocalizableMessage);
    }

    @Test
    public void control_string_ok() {
        List<Localizable> messages = new ArrayList<>();
        service.control(messages, "Present", "any");
        assertTrue("No message was added", messages.isEmpty());
    }

    @Test
    public void control_string_null_nok() {
        List<Localizable> messages = new ArrayList<>();
        service.control(messages, null, "any");
        assertEquals("One message was added", 1, messages.size());
        assertTrue(messages.get(0) instanceof LocalizableMessage);
    }

    @Test
    public void control_string_blank_nok() {
        List<Localizable> messages = new ArrayList<>();
        service.control(messages, "", "any");
        assertEquals("One message was added", 1, messages.size());
        assertTrue(messages.get(0) instanceof LocalizableMessage);
    }

    @Test
    public void controlInvoice_profile_ok() {
        InvoiceData data = InvoiceFixtures.invoiceData();
        service.controlInvoice(data);
    }

    @Test
    public void controlInvoice_profile_nok() {
        InvoiceData data = InvoiceFixtures.invoiceIncompleteData();
        try {
            service.controlInvoice(data);
            fail("Should have failed.");
        } catch (InvoiceControlException ex) {
            String message = ex.getLocalizedMessage(strings, Locale.ENGLISH);
            assertEquals("The invoice cannot be generated because of the following problems:\n" +
                    " \n" +
                    " - The BIC must be filled in.\n" +
                    " - The IBAN must be filled in.\n" +
                    " - The VAT identifier of your company must be filled in.\n" +
                    " - The postal address of your company must be filled in.\n" +
                    " - The phone of your company must be filled in.\n" +
                    " - Your company name must be filled in.\n" +
                    " - The postal address of the school must be filled in.\n" +
                    " - The VAT identifier of the school must be filled in.\n" +
                    " - No hourly rate is defined for the school.\n" +
                    " - No hours for this school and this period.\n" +
                    "\n" +
                    " \n" +
                    " You can correct those problems by either editing your profile (accessible via the user menu) or by modifying the school parameters.", message);
        }
    }

}
