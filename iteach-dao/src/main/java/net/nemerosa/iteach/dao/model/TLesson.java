package net.nemerosa.iteach.dao.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TLesson {

    private final int id;
    private final int teacherId;
    private final int studentId;
    private final LocalDate planningDate;
    private final LocalTime planningFrom;
    private final LocalTime planningTo;
    private final String location;

}
