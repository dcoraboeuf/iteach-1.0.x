package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.PreferencesRepository;
import net.nemerosa.iteach.service.PreferencesService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.CalendarPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@Transactional
public class PreferencesServiceImpl implements PreferencesService {

    private static final String INVOICE_STUDENT_DETAIL = "invoice.student.detail";
    private static final String CALENDAR_MIN_TIME = "calendar.minTime";
    private static final String CALENDAR_MAX_TIME = "calendar.maxTime";

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

    @Override
    public CalendarPreferences getCalendarPreferences() {
        int teacherId = securityUtils.checkTeacher();
        return new CalendarPreferences(
                preferencesRepository.getTime(teacherId, CALENDAR_MIN_TIME, LocalTime.of(7, 0)),
                preferencesRepository.getTime(teacherId, CALENDAR_MAX_TIME, LocalTime.of(21, 0))
        );
    }

    @Override
    public Ack setCalendarPreferences(CalendarPreferences preferences) {
        int teacherId = securityUtils.checkTeacher();
        preferencesRepository.setTime(teacherId, CALENDAR_MIN_TIME, preferences.getMinTime());
        preferencesRepository.setTime(teacherId, CALENDAR_MAX_TIME, preferences.getMaxTime());
        return Ack.OK;
    }
}
