package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.model.EmployeePairResult;
import com.stoyankirkov.employees.model.EmployeeProject;
import com.stoyankirkov.employees.service.PairProjectsProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PairProjectsProcessorImpl implements PairProjectsProcessor {

    @Override
    public List<EmployeePairResult> findTop(List<EmployeeProject> projects) {
        log.info("Searching for top pair and their projects.");
        List<EmployeePairResult> pairs = collectPairs(projects);
        log.info("Found {} pairs.", pairs.size());

        Map<String, Long> pairTotalDays = new HashMap<>();
        for (EmployeePairResult pair : pairs) {
            long minEmp = Math.min(pair.getEmployeeId1(), pair.getEmployeeId2());
            long maxEmp = Math.max(pair.getEmployeeId1(), pair.getEmployeeId2());
            String key = minEmp + "-" + maxEmp;
            pairTotalDays.put(key, pairTotalDays.getOrDefault(key, 0L) + pair.getDaysWorked());
        }

        long maxDays = pairTotalDays.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        List<EmployeePairResult> topPairProjects = pairs.stream()
                .filter(result -> {
                    long minEmp = Math.min(result.getEmployeeId1(), result.getEmployeeId2());
                    long maxEmp = Math.max(result.getEmployeeId1(), result.getEmployeeId2());
                    String key = minEmp + "-" + maxEmp;
                    return pairTotalDays.get(key) == maxDays;
                })
                .collect(Collectors.toList());
        log.info("Top pair worked on {} projects.", topPairProjects.size());
        return topPairProjects;
    }

    private List<EmployeePairResult> collectPairs(List<EmployeeProject> projects) {
        List<EmployeePairResult> results = new ArrayList<>();

        Map<Long, List<EmployeeProject>> projectsById = new HashMap<>();
        for (EmployeeProject project : projects) {
            projectsById.computeIfAbsent(project.getProjectId(), k -> new ArrayList<>()).add(project);
        }

        for (Map.Entry<Long, List<EmployeeProject>> entry : projectsById.entrySet()) {
            long projectId = entry.getKey();
            List<EmployeeProject> employeeProjectList = entry.getValue();

            for (int i = 0; i < employeeProjectList.size() - 1; i++) {
                for (int j = i + 1; j < employeeProjectList.size(); j++) {
                    EmployeeProject project1 = employeeProjectList.get(i);
                    EmployeeProject project2 = employeeProjectList.get(j);
                    long overlap = calculateOverlapDays(
                            project1.getDateFrom(),
                            project1.getDateTo(),
                            project2.getDateFrom(),
                            project2.getDateTo()
                    );
                    if (overlap > 0) {
                        results.add(
                                EmployeePairResult.builder()
                                        .employeeId1(project1.getEmployeeId())
                                        .employeeId2(project2.getEmployeeId())
                                        .projectId(projectId)
                                        .daysWorked(overlap)
                                        .build()
                        );
                    }
                }
            }
        }
        return results;
    }

    private long calculateOverlapDays(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        LocalDate maxStart = start1.isAfter(start2) ? start1 : start2;
        LocalDate minEnd = end1.isBefore(end2) ? end1 : end2;
        return maxStart.isBefore(minEnd) || maxStart.isEqual(minEnd) ?
                ChronoUnit.DAYS.between(maxStart, minEnd) + 1 : 0;
    }
}
