package net.nemerosa.iteach.ui.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class UICalendarPreferences {

    @NotNull
    private final LocalTime minTime;
    @NotNull
    private final LocalTime maxTime;
    private final boolean weekEnds;

}
