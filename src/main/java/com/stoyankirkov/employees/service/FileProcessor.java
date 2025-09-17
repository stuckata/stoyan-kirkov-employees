package com.stoyankirkov.employees.service;

import com.stoyankirkov.employees.model.EmployeePairResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileProcessor {

    List<EmployeePairResult> process(MultipartFile file) throws IOException;
}
