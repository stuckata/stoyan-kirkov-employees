package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.service.DateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DateParserImpl implements DateParser {

    private static final List<DateTimeFormatter> DATE_FORMATS = new ArrayList<>();

    @Value("#{${date.formats}}")
    private List<String> dateFormats;

    @Override
    public LocalDate parse(String str) {
        if (DATE_FORMATS.isEmpty()) {
            for (String format : dateFormats) {
                DATE_FORMATS.add(DateTimeFormatter.ofPattern(format));
            }
        }
        log.debug("Parsing date: {}", str);
        if (str == null || str.isBlank() || str.equalsIgnoreCase("NULL")) {
            return LocalDate.now();
        }
        for (DateTimeFormatter fmt : DATE_FORMATS) {
            try {
                return LocalDate.parse(str.trim(), fmt);
            } catch (Exception ignored) {
                // intentionally ignore exceptions while searching for the date format
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + str);
    }
}
