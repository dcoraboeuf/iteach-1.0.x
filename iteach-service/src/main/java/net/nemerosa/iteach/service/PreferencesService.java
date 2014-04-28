package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.model.CalendarPreferences;

public interface PreferencesService {

    boolean getInvoiceStudentDetail();

    void setInvoiceStudentDetail(boolean value);

    CalendarPreferences getCalendarPreferences();

    Ack setCalendarPreferences(CalendarPreferences preferences);

}
