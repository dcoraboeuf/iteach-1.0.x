package net.nemerosa.iteach.service.io.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class XComment {
    private final LocalDateTime creation;
    private final LocalDateTime update;
    private final String rawContent;
}
