package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.InvoiceService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.InvoiceData;
import net.nemerosa.iteach.service.model.InvoiceForm;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolReport;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.Future;

import static net.nemerosa.iteach.service.impl.PeriodUtils.toPeriod;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final TeacherService teacherService;
    private final AccountService accountService;
    private final InvoiceRepository invoiceRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public InvoiceServiceImpl(TeacherService teacherService, AccountService accountService, InvoiceRepository invoiceRepository, SecurityUtils securityUtils) {
        this.teacherService = teacherService;
        this.accountService = accountService;
        this.invoiceRepository = invoiceRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Future<Integer> generate(InvoiceForm invoiceForm, String type) {
        // Gets the invoice data
        InvoiceData invoiceData = getInvoiceData(invoiceForm);
        // FIXME Method net.nemerosa.iteach.service.impl.InvoiceServiceImpl.generate
        return null;
    }

    protected InvoiceData getInvoiceData(InvoiceForm form) {
        AccountAuthentication account = securityUtils.getCurrentAccount();
        SchoolReport report = teacherService.getSchoolReport(form.getSchoolId(), toPeriod(form.getPeriod()), true);
        School school = teacherService.getSchool(form.getSchoolId());
        // VAT support
        Money vat;
        Money vatTotal;
        BigDecimal vatRate = school.getVatRate();
        if (vatRate != null) {
            vatRate = vatRate.movePointLeft(2);
            vat = report.getIncome().multipliedBy(vatRate, RoundingMode.HALF_UP);
            vatTotal = report.getIncome().plus(vat);
        } else {
            vat = Money.zero(report.getIncome().getCurrencyUnit());
            vatTotal = report.getIncome();
        }
        // OK
        return new InvoiceData(
                form.getPeriod(),
                LocalDate.now(),
                form.getNumber(),
                account.getName(),
                account.getEmail(),
                accountService.getProfile(),
                school,
                report,
                vat,
                vatTotal
        );
    }
}
