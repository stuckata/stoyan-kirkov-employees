package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.model.EmployeeProject;
import com.stoyankirkov.employees.service.DateParser;
import com.stoyankirkov.employees.service.FileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsvFileReader implements FileReader {

    private final DateParser dateParser;

    @Override
    public List<EmployeeProject> read(MultipartFile file) throws IOException {
        log.info("Reading CSV file.");
        List<EmployeeProject> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get())) {

            for (CSVRecord record : parser) {
                long empId = Long.parseLong(record.get("EmpID").trim());
                long projectId = Long.parseLong(record.get("ProjectID").trim());
                LocalDate dateFrom = this.dateParser.parse(record.get("DateFrom"));
                LocalDate dateTo = this.dateParser.parse(record.get("DateTo"));

                list.add(EmployeeProject.builder()
                        .employeeId(empId)
                        .projectId(projectId)
                        .dateFrom(dateFrom)
                        .dateTo(dateTo)
                        .build());
            }
        }
        log.info("CSV file successfully read.");
        return list;
    }
}
