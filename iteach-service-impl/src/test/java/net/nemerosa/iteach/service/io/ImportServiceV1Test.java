package net.nemerosa.iteach.service.io;

import org.junit.Test;

import java.time.LocalDateTime;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static org.junit.Assert.assertEquals;

public class ImportServiceV1Test {

    @Test
    public void getTime() {
        assertEquals(
                LocalDateTime.of(2013, 10, 9, 14, 0), // UTC
                new ImportServiceV1(null, null, null).getTime("2013-10-09", "16:00")
        );
    }

    @Test
    public void getTimeFromJson() {
        assertEquals(
                LocalDateTime.of(2013, 10, 9, 14, 0), // UTC
                new ImportServiceV1(null, null, null).getTime(
                        object()
                                .with("date", "2013-10-09")
                                .with("from", "16:00")
                                .end(),
                        "date",
                        "from"
                )
        );
    }

}
