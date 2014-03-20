package net.nemerosa.iteach.ui.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UILessonForm {

    @Min(1)
    private final int studentId;
    @Size(min = 0, max = 80)
    private final String location;
    @NotNull
    private final LocalDateTime from;
    @NotNull
    private final LocalDateTime to;


}
