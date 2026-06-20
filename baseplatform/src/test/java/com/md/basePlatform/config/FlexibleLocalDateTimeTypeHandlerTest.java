package com.md.basePlatform.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FlexibleLocalDateTimeTypeHandlerTest {

    private final FlexibleLocalDateTimeTypeHandler handler = new FlexibleLocalDateTimeTypeHandler();

    @Test
    void should_read_iso_string() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("c")).thenReturn("2026-05-06T11:28:56.800");

        LocalDateTime t = handler.getNullableResult(rs, "c");

        assertNotNull(t);
        assertEquals(2026, t.getYear());
        assertEquals(5, t.getMonthValue());
    }

    @Test
    void should_read_timestamp() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        LocalDateTime expected = LocalDateTime.of(2025, 1, 2, 3, 4, 5);
        when(rs.getObject("c")).thenReturn(Timestamp.valueOf(expected));

        LocalDateTime t = handler.getNullableResult(rs, "c");

        assertEquals(expected, t);
    }

    @Test
    void should_read_space_separated_string() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("c")).thenReturn("2026-05-06 11:28:56");

        LocalDateTime t = handler.getNullableResult(rs, "c");

        assertEquals(2026, t.getYear());
        assertEquals(5, t.getMonthValue());
        assertEquals(6, t.getDayOfMonth());
    }
}
