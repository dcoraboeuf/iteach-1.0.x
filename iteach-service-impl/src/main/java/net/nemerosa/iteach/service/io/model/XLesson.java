package net.nemerosa.iteach.service.io.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class XLesson {
    private final String location;
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final List<XComment> comments;
}
