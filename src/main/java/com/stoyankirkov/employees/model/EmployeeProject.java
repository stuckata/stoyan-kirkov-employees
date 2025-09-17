package com.stoyankirkov.employees.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class EmployeeProject {
    private long employeeId;
    private long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
