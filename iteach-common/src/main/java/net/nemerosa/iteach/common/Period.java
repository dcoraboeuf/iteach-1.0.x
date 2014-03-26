package net.nemerosa.iteach.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Period {

    private LocalDateTime from;
    private LocalDateTime to;

}
