package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UILessonFilter {

    private Integer studentId;
    private LocalDateTime from;
    private LocalDateTime to;

}
