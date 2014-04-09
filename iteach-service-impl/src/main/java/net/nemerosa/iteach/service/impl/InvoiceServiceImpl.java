package net.nemerosa.iteach.service.impl;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.service.*;
import net.nemerosa.iteach.service.invoice.InvoiceGenerator;
import net.nemerosa.iteach.service.model.*;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static net.nemerosa.iteach.service.impl.PeriodUtils.toPeriod;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final TeacherService teacherService;
    private final AccountService accountService;
    private final Map<String, InvoiceGenerator> generators;
    private final InvoiceRepository invoiceRepository;
    private final SecurityUtils securityUtils;
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            5,
            new ThreadFactoryBuilder()
                    .setNameFormat("invoice-generator-%d")
                    .setDaemon(true)
                    .build());

    @Autowired
    public InvoiceServiceImpl(TeacherService teacherService, AccountService accountService, Collection<InvoiceGenerator> generators, InvoiceRepository invoiceRepository, SecurityUtils securityUtils) {
        this.teacherService = teacherService;
        this.accountService = accountService;
        this.invoiceRepository = invoiceRepository;
        this.securityUtils = securityUtils;
        this.generators = Maps.uniqueIndex(
                generators,
                InvoiceGenerator::getType
        );
    }

    @Override
    public Future<Integer> generate(InvoiceForm invoiceForm, String type, Locale locale) {
        // Gets the invoice data
        InvoiceData invoiceData = getInvoiceData(invoiceForm);
        // Gets a generator for the type
        InvoiceGenerator generator = getInvoiceGenerator(type);
        // Asynchronous generation
        return executorService.submit(
                (Callable<Integer>) () -> generate(invoiceData, generator, locale)
        );
    }

    @Override
    public List<InvoiceInfo> getInvoices(Integer school, Integer year) {
        return invoiceRepository.list(securityUtils.checkTeacher(), school, year)
                .stream()
                .map(t -> new InvoiceInfo(
                        t.getId(),
                        t.getSchool(),
                        YearMonth.of(t.getYear(), t.getMonth()),
                        t.getNumber(),
                        t.getGeneration(),
                        t.isDownloaded(),
                        t.getDocumentType()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void downloadInvoice(int invoiceId, OutputStream out) {
        int teacherId = securityUtils.checkTeacher();
        invoiceRepository.download(teacherId, invoiceId, out);
    }

    @Override
    public void invoiceIsDownloaded(int invoiceId) {
        int teacherId = securityUtils.checkTeacher();
        invoiceRepository.downloaded(teacherId, invoiceId);
    }

    @Override
    public long getNextInvoiceNumber() {
        int teacherId = securityUtils.checkTeacher();
        Long lastInvoiceNumber = invoiceRepository.getLastInvoiceNumber(teacherId);
        if (lastInvoiceNumber != null) {
            return lastInvoiceNumber + 1;
        } else {
            return 1;
        }
    }

    protected int generate(InvoiceData data, InvoiceGenerator generator, Locale locale) {
        // Generation of the content
        byte[] document = generator.generate(data, locale);
        // Saving in repository
        return invoiceRepository.save(
                data.getTeacherId(),
                data.getSchool().getId(),
                data.getPeriod().getYear(),
                data.getPeriod().getMonth(),
                data.getNumber(),
                generator.getType(),
                document
        );
    }

    protected InvoiceGenerator getInvoiceGenerator(String type) {
        InvoiceGenerator generator = generators.get(type);
        if (generator != null) {
            return generator;
        } else {
            throw new InvoiceGeneratorTypeNotFoundException(type);
        }
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
                account.getId(),
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
