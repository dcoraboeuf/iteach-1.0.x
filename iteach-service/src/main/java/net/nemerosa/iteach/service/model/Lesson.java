package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Lesson {

    private final int id;
    private final int teacherId;
    private final int studentId;
    private final String location;
    private final LocalDateTime from;
    private final LocalDateTime to;

}
