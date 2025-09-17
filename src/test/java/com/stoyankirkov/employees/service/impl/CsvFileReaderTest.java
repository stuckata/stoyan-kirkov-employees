package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.model.EmployeeProject;
import com.stoyankirkov.employees.service.DateParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvFileReaderTest {

    @Mock
    private DateParser dateParser;
    @InjectMocks
    private CsvFileReader csvFileReader;

    @Test
    void testReadCsvFile() throws IOException {

        String csvContent = """
                EmpID,ProjectID,DateFrom,DateTo
                101,1,2020-01-01,2020-01-10
                102,2,2020-02-01,2020-02-15
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(this.dateParser.parse("2020-01-01"))
                .thenReturn(LocalDate.of(2020, 1, 1));
        when(this.dateParser.parse("2020-01-10"))
                .thenReturn(LocalDate.of(2020, 1, 10));
        when(this.dateParser.parse("2020-02-01"))
                .thenReturn(LocalDate.of(2020, 2, 1));
        when(this.dateParser.parse("2020-02-15"))
                .thenReturn(LocalDate.of(2020, 2, 15));

        List<EmployeeProject> result = this.csvFileReader.read(file);

        assertEquals(2, result.size());

        EmployeeProject first = result.getFirst();
        assertEquals(101, first.getEmployeeId());
        assertEquals(1, first.getProjectId());
        assertEquals(LocalDate.of(2020, 1, 1), first.getDateFrom());
        assertEquals(LocalDate.of(2020, 1, 10), first.getDateTo());

        EmployeeProject second = result.get(1);
        assertEquals(102, second.getEmployeeId());
        assertEquals(2, second.getProjectId());
        assertEquals(LocalDate.of(2020, 2, 1), second.getDateFrom());
        assertEquals(LocalDate.of(2020, 2, 15), second.getDateTo());

        verify(this.dateParser, times(4)).parse(anyString());
    }
}