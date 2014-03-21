package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UILessonFilter {

    private final Integer studentId;
    private final LocalDateTime from;
    private final LocalDateTime to;

}
