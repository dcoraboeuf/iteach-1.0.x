package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CalendarPreferences {

    private final LocalTime minTime;
    private final LocalTime maxTime;
    private final boolean weekEnds;

}
