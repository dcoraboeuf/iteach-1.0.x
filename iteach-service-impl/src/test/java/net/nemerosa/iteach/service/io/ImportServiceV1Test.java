package net.nemerosa.iteach.service.io;

import org.junit.Test;

import java.time.LocalDateTime;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static org.junit.Assert.assertEquals;

public class ImportServiceV1Test {

    @Test
    public void getTime() {
        assertEquals(
                LocalDateTime.of(2014, 3, 24, 18, 34),
                new ImportServiceV1(null, null, null).getTime("2014-03-24", "18:34")
        );
    }

    @Test
    public void getTimeFromJson() {
        assertEquals(
                LocalDateTime.of(2014, 3, 24, 18, 34),
                new ImportServiceV1(null, null, null).getTime(
                        object()
                                .with("date", "2014-03-24")
                                .with("from", "18:34")
                                .end(),
                        "date",
                        "from"
                )
        );
    }

}
