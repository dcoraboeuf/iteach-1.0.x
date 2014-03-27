package net.nemerosa.iteach.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class Lesson {

    private final int id;
    private final int teacherId;
    private final int studentId;
    private final String location;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public BigDecimal getDurationInHours() {
        return BigDecimal.valueOf(Duration.between(from, to).toMinutes()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
