package net.nemerosa.iteach.dao.jdbc;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SQLUtilsTest {

    @Test
    public void getLocalDateTimeFromDB_null() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("c")).thenReturn(null);
        assertNull(SQLUtils.getLocalDateTimeFromDB(rs, "c"));
    }

    @Test
    public void getLocalDateTimeFromDB() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("c")).thenReturn("2014-03-20T21:09:00");
        assertEquals(
                LocalDateTime.of(2014, 3, 20, 21, 9),
                SQLUtils.getLocalDateTimeFromDB(rs, "c")
        );
    }

    @Test
    public void getDBValueFromLocalDateTime_null() {
        assertNull(SQLUtils.getDBValueFromLocalDateTime(null));
    }

    @Test
    public void getDBValueFromLocalDateTime() {
        assertEquals(
                "2014-03-20T21:39",
                SQLUtils.getDBValueFromLocalDateTime(LocalDateTime.of(2014, 3, 20, 21, 39))
        );
    }

}
