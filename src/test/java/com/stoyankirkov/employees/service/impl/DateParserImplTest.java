package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.service.DateParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = DateParserImpl.class)
class DateParserImplTest {

    @Autowired
    private DateParser dateParser;

    @ParameterizedTest(name = "{index} => date=''{0}'' expected={1}-{2}-{3}")
    @CsvSource({
            "2023-09-17, 2023, 9, 17",
            "17-09-2023, 2023, 9, 17",
            "09-17-2023, 2023, 9, 17",
            "2023/09/17, 2023, 9, 17",
            "17/09/2023, 2023, 9, 17",
            "09/17/2023, 2023, 9, 17",
            "2023.09.17, 2023, 9, 17",
            "17.09.2023, 2023, 9, 17",
            "17 Sep 2023, 2023, 9, 17",
            "17-Sep-2023, 2023, 9, 17",
            "'Sep 17, 2023', 2023, 9, 17",
            "17 September 2023, 2023, 9, 17",
            "20230917, 2023, 9, 17"
    })
    void testParseAllFormats(String input, int expectedYear, int expectedMonth, int expectedDay) {
        LocalDate result = this.dateParser.parse(input);
        assertEquals(LocalDate.of(expectedYear, expectedMonth, expectedDay), result);
    }

    @ParameterizedTest
    @CsvSource({
            "'',",
            "NULL,"
    })
    void testParseNullOrBlankReturnsToday(String input) {
        LocalDate result = this.dateParser.parse(input);
        assertEquals(LocalDate.now(), result);
    }

    @ParameterizedTest
    @CsvSource({
            "invalidDate",
            "32-13-2023"
    })
    void testUnsupportedFormatThrowsException(String input) {
        assertThrows(IllegalArgumentException.class, () -> this.dateParser.parse(input));
    }
}