package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.PreferencesRepository;
import net.nemerosa.iteach.service.PreferencesService;
import net.nemerosa.iteach.service.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreferencesServiceImpl implements PreferencesService {

    private static final String INVOICE_STUDENT_DETAIL = "invoice.student.detail";

    private final PreferencesRepository preferencesRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public PreferencesServiceImpl(PreferencesRepository preferencesRepository, SecurityUtils securityUtils) {
        this.preferencesRepository = preferencesRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public boolean getInvoiceStudentDetail() {
        return preferencesRepository.getBoolean(securityUtils.checkTeacher(), INVOICE_STUDENT_DETAIL, false);
    }

    @Override
    public void setInvoiceStudentDetail(boolean value) {
        preferencesRepository.setBoolean(securityUtils.checkTeacher(), INVOICE_STUDENT_DETAIL, value);
    }
}
