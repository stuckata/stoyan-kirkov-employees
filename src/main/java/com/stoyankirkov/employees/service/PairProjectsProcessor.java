package com.stoyankirkov.employees.service;

import com.stoyankirkov.employees.model.EmployeePairResult;
import com.stoyankirkov.employees.model.EmployeeProject;

import java.util.List;

public interface PairProjectsProcessor {

    List<EmployeePairResult> findTop(List<EmployeeProject> projects);
}
