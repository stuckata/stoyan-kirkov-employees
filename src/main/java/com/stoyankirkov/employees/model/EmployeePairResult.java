package com.stoyankirkov.employees.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeePairResult {
    private long employeeId1;
    private long employeeId2;
    private long projectId;
    private long daysWorked;
}
