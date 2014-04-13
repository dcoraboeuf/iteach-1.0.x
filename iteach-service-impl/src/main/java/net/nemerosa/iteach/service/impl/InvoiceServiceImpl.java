package net.nemerosa.iteach.service.impl;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.nemerosa.iteach.common.*;
import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.dao.model.TInvoice;
import net.nemerosa.iteach.service.*;
import net.nemerosa.iteach.service.invoice.InvoiceGenerator;
import net.nemerosa.iteach.service.model.*;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import net.sf.jstring.MultiLocalizable;
import net.sf.jstring.Strings;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static net.nemerosa.iteach.service.impl.PeriodUtils.toPeriod;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final TeacherService teacherService;
    private final AccountService accountService;
    private final Map<String, InvoiceGenerator> generators;
    private final InvoiceRepository invoiceRepository;
    private final TransactionTemplate transactionTemplate;
    private final SecurityUtils securityUtils;
    private final Strings strings;
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            5,
            new ThreadFactoryBuilder()
                    .setNameFormat("invoice-generator-%d")
                    .setDaemon(true)
                    .build()
    );

    @Autowired
    public InvoiceServiceImpl(TeacherService teacherService, AccountService accountService, Collection<InvoiceGenerator> generators, InvoiceRepository invoiceRepository, SecurityUtils securityUtils, PlatformTransactionManager transactionManager, Strings strings) {
        this.teacherService = teacherService;
        this.accountService = accountService;
        this.invoiceRepository = invoiceRepository;
        this.securityUtils = securityUtils;
        this.strings = strings;
        this.generators = Maps.uniqueIndex(
                generators,
                InvoiceGenerator::getType
        );
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public InvoiceInfo generate(InvoiceForm invoiceForm, String type, Locale locale) {
        // Gets the invoice data
        InvoiceData data = getInvoiceData(invoiceForm);
        // Controls the data
        List<Localizable> messages = controlInvoice(data);
        if (!messages.isEmpty()) {
            return new InvoiceInfo(
                    0,
                    InvoiceStatus.ERROR,
                    new LocalizableMessage(
                            "net.nemerosa.iteach.service.InvoiceService.control",
                            new MultiLocalizable(messages)
                    ).getLocalizedMessage(strings, locale).trim(),
                    null,
                    invoiceForm.getSchoolId(),
                    invoiceForm.getPeriod(),
                    invoiceForm.getNumber(),
                    LocalDateTime.now(),
                    false,
                    type
            );
        }
        // Gets a generator for the type
        InvoiceGenerator generator = getInvoiceGenerator(type);
        // Generation date
        LocalDateTime generation = LocalDateTime.now(ZoneOffset.UTC);
        // Creates a stub in the repository
        int id = invoiceRepository.create(
                data.getTeacherId(),
                data.getSchool().getId(),
                data.getPeriod().getYear(),
                data.getPeriod().getMonthValue(),
                data.getNumber(),
                generator.getType(),
                generation
        );
        // Stub info
        InvoiceInfo info = new InvoiceInfo(
                id,
                InvoiceStatus.CREATED,
                null,
                null,
                invoiceForm.getSchoolId(),
                invoiceForm.getPeriod(),
                invoiceForm.getNumber(),
                generation,
                false,
                type
        );
        // Asynchronous generation
        logger.debug("[invoice] Scheduling generation for #{}", id);
        executorService.submit(() -> generate(id, data, generator, locale));
        // OK
        return info;
    }

    protected List<Localizable> controlInvoice(InvoiceData data) {
        List<Localizable> messages = new ArrayList<>();
        // Profile controls
        control(messages, data.getProfile().getBic(), "control.profile.bic");
        control(messages, data.getProfile().getIban(), "control.profile.iban");
        control(messages, data.getProfile().getVat(), "control.profile.vat");
        control(messages, data.getProfile().getPostalAddress(), "control.profile.postalAddress");
        control(messages, data.getProfile().getPhone(), "control.profile.phone");
        control(messages, data.getProfile().getCompany(), "control.profile.company");
        // School controls
        control(messages, data.getSchool().getPostalAddress(), "control.school.postalAddress");
        control(messages, data.getSchool().getVat(), "control.school.vat");
        control(messages, data.getSchool().getHourlyRate() != null, "control.school.hourlyRate");
        // Report
        control(messages, data.getReport().getHours().compareTo(BigDecimal.ZERO) > 0, "control.hours");
        // End
        return messages;
    }

    protected void control(List<Localizable> messages, String value, String keySuffix) {
        control(messages, StringUtils.isNotBlank(value), keySuffix);
    }

    protected void control(List<Localizable> messages, boolean test, String keySuffix) {
        if (!test) {
            messages.add(
                    new LocalizableMessage(
                            InvoiceService.class.getName() + "." + keySuffix
                    )
            );
        }
    }

    @Override
    public List<InvoiceInfo> getInvoices(InvoiceFilter filter) {
        return invoiceRepository.list(
                securityUtils.checkTeacher(),
                filter.getSchoolId(),
                filter.getYear(),
                filter.getDownloaded(),
                filter.getStatus()
        )
                .stream()
                .map(this::toInvoiceInfo)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalCount() {
        return invoiceRepository.totalCount(securityUtils.checkTeacher());
    }

    @Override
    public InvoiceInfo getInvoiceInfo(int id) {
        return toInvoiceInfo(
                invoiceRepository.getById(securityUtils.checkTeacher(), id)
        );
    }

    private InvoiceInfo toInvoiceInfo(TInvoice t) {
        return new InvoiceInfo(
                t.getId(),
                t.getStatus(),
                t.getErrorMessage(),
                t.getErrorUuid(),
                t.getSchool(),
                YearMonth.of(t.getYear(), t.getMonth()),
                t.getNumber(),
                t.getGeneration(),
                t.isDownloaded(),
                t.getDocumentType()
        );
    }

    @Override
    public Document downloadInvoice(int invoiceId) {
        int teacherId = securityUtils.checkTeacher();
        InvoiceInfo info = getInvoiceInfo(invoiceId);
        UntitledDocument document = invoiceRepository.download(teacherId, invoiceId);
        return new Document(
                document,
                computeDocumentTitle(info),
                "pdf" // TODO Support something else than PDF
        );
    }

    private String computeDocumentTitle(InvoiceInfo info) {
        return String.format(
                "%s-%s-%d",
                teacherService.getSchool(info.getSchoolId()).getName(),
                DateTimeFormatter.ofPattern("yyyyMM").format(info.getPeriod()),
                info.getNumber()
        );
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

    @Override
    public Ack deleteInvoices(List<Integer> ids) {
        int teacherId = securityUtils.checkTeacher();
        for (int id : ids) {
            invoiceRepository.delete(teacherId, id);
        }
        return Ack.OK;
    }

    protected void generate(int id, InvoiceData data, InvoiceGenerator generator, Locale locale) {
        try {
            // Starts the generation
            logger.debug("[invoice] Starting generation for #{}", id);
            transactionTemplate.execute(status -> {
                invoiceRepository.startGeneration(data.getTeacherId(), id);
                return null;
            });
            // Generation of the content
            logger.debug("[invoice] Generating document for #{}", id);
            byte[] document = generator.generate(data, locale);
            logger.debug("[invoice] Document for #{} has been generated", id);
            // Saving the document in the repository
            transactionTemplate.execute(status -> {
                invoiceRepository.save(
                        data.getTeacherId(),
                        id,
                        document
                );
                return null;
            });
            logger.debug("[invoice] Generation for #{} is done.", id);
        } catch (InputException ex) {
            // Gets the localized message
            String message = ex.getLocalizedMessage(strings, locale);
            logger.debug("[invoice] Input problem for #{}: {}", id, message);
            // Sets the status to ERROR and this message
            transactionTemplate.execute(status -> {
                invoiceRepository.error(data.getTeacherId(), id, message, null);
                return null;
            });
        } catch (Exception ex) {
            // UUID for the error
            String uuid = UUID.randomUUID().toString();
            // Generates an indexed stack trace in the logs
            logger.error(String.format("[invoice] [error] id=%d, uuid=%s", id, uuid), ex);
            // Message to store
            String message = strings.get(locale, "net.nemerosa.iteach.service.InvoiceService.error.uuid", uuid);
            // Marks the invoice in error together with its ID
            transactionTemplate.execute(status -> {
                invoiceRepository.error(data.getTeacherId(), id, message, uuid);
                return null;
            });
        }
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
