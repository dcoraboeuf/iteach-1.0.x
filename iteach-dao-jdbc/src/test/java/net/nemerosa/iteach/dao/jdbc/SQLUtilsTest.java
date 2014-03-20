package net.nemerosa.iteach.dao.jdbc;

import org.junit.Test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SQLUtilsTest {

    @Test
    public void getLocalDateTimeFromDB_null() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getDate("c")).thenReturn(null);
        assertNull(SQLUtils.getLocalDateTimeFromDB(rs, "c"));
    }

    @Test
    public void getLocalDateTimeFromDB() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getDate("c")).thenReturn(new Date(LocalDateTime.of(2014, 3, 20, 21, 39).toInstant(ZoneOffset.UTC).toEpochMilli()));
        assertEquals(
                LocalDateTime.of(2014, 3, 20, 21, 39),
                SQLUtils.getLocalDateTimeFromDB(rs, "c")
        );
    }

    @Test
    public void getDBValueFromLocalDateTime_null() {
        assertNull(SQLUtils.getDBValueFromLocalDateTime(null));
    }

    @Test
    public void getDBValueFromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2014, 3, 20, 21, 39);
        Date d = SQLUtils.getDBValueFromLocalDateTime(ldt);
        assertEquals(
                ldt.toInstant(ZoneOffset.UTC).toEpochMilli(),
                d.getTime()
        );
    }

}
