package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.model.EmployeePairResult;
import com.stoyankirkov.employees.model.EmployeeProject;
import com.stoyankirkov.employees.service.PairProjectsProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PairProjectsProcessorImpl.class)
class PairProjectsProcessorImplTest {

    @Autowired
    private PairProjectsProcessor pairProjectsProcessor;

    @Test
    void testSinglePairOverlap() {
        EmployeeProject p1 = EmployeeProject.builder()
                .employeeId(1)
                .projectId(100)
                .dateFrom(LocalDate.of(2020, 1, 1))
                .dateTo(LocalDate.of(2020, 1, 10))
                .build();
        EmployeeProject p2 = EmployeeProject.builder()
                .employeeId(2)
                .projectId(100)
                .dateFrom(LocalDate.of(2020, 1, 5))
                .dateTo(LocalDate.of(2020, 1, 15))
                .build();

        List<EmployeePairResult> result = this.pairProjectsProcessor.findTop(List.of(p1, p2));

        assertEquals(1, result.size());
        EmployeePairResult pair = result.getFirst();
        assertEquals(1, pair.getEmployeeId1());
        assertEquals(2, pair.getEmployeeId2());
        assertEquals(100, pair.getProjectId());
        assertEquals(6, pair.getDaysWorked());
    }

    @Test
    void testNoOverlap() {
        EmployeeProject p1 = EmployeeProject.builder()
                .employeeId(1)
                .projectId(200)
                .dateFrom(LocalDate.of(2020, 1, 1))
                .dateTo(LocalDate.of(2020, 1, 5))
                .build();
        EmployeeProject p2 = EmployeeProject.builder()
                .employeeId(2)
                .projectId(200)
                .dateFrom(LocalDate.of(2020, 1, 6))
                .dateTo(LocalDate.of(2020, 1, 10))
                .build();

        List<EmployeePairResult> result = this.pairProjectsProcessor.findTop(List.of(p1, p2));

        assertEquals(0, result.size());
    }

    @Test
    void testMultipleProjectsSamePair() {
        EmployeeProject p1 = EmployeeProject.builder()
                .employeeId(1)
                .projectId(300)
                .dateFrom(LocalDate.of(2020, 1, 1))
                .dateTo(LocalDate.of(2020, 1, 10))
                .build();
        EmployeeProject p2 = EmployeeProject.builder()
                .employeeId(2)
                .projectId(300)
                .dateFrom(LocalDate.of(2020, 1, 5))
                .dateTo(LocalDate.of(2020, 1, 15))
                .build();
        EmployeeProject p3 = EmployeeProject.builder()
                .employeeId(1)
                .projectId(301)
                .dateFrom(LocalDate.of(2020, 2, 1))
                .dateTo(LocalDate.of(2020, 2, 5))
                .build();
        EmployeeProject p4 = EmployeeProject.builder()
                .employeeId(2)
                .projectId(301)
                .dateFrom(LocalDate.of(2020, 2, 3))
                .dateTo(LocalDate.of(2020, 2, 10))
                .build();

        List<EmployeePairResult> result = this.pairProjectsProcessor.findTop(List.of(p1, p2, p3, p4));

        assertEquals(2, result.size());
        long totalDays = result.stream()
                .mapToLong(EmployeePairResult::getDaysWorked)
                .sum();
        assertEquals(9, totalDays);
    }

    @Test
    void testMultiplePairsFindTop() {
        EmployeeProject p1 = EmployeeProject.builder()
                .employeeId(1)
                .projectId(400)
                .dateFrom(LocalDate.of(2020, 1, 1))
                .dateTo(LocalDate.of(2020, 1, 5))
                .build();
        EmployeeProject p2 = EmployeeProject.builder()
                .employeeId(2)
                .projectId(400)
                .dateFrom(LocalDate.of(2020, 1, 2))
                .dateTo(LocalDate.of(2020, 1, 3))
                .build();
        EmployeeProject p3 = EmployeeProject.builder()
                .employeeId(3)
                .projectId(401)
                .dateFrom(LocalDate.of(2020, 2, 1))
                .dateTo(LocalDate.of(2020, 2, 10))
                .build();
        EmployeeProject p4 = EmployeeProject.builder()
                .employeeId(4)
                .projectId(401)
                .dateFrom(LocalDate.of(2020, 2, 5))
                .dateTo(LocalDate.of(2020, 2, 10))
                .build();

        List<EmployeePairResult> result = this.pairProjectsProcessor.findTop(List.of(p1, p2, p3, p4));

        assertEquals(1, result.size());
        EmployeePairResult r = result.getFirst();
        assertEquals(3, r.getEmployeeId1());
        assertEquals(4, r.getEmployeeId2());
        assertEquals(401, r.getProjectId());
        assertEquals(6, r.getDaysWorked());
    }
}