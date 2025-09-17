package com.stoyankirkov.employees.service.impl;

import com.stoyankirkov.employees.model.EmployeePairResult;
import com.stoyankirkov.employees.model.EmployeeProject;
import com.stoyankirkov.employees.service.FileProcessor;
import com.stoyankirkov.employees.service.FileReader;
import com.stoyankirkov.employees.service.PairProjectsProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CsvFileProcessor implements FileProcessor {

    private final FileReader fileReader;
    private final PairProjectsProcessor pairProjectsProcessor;

    @Override
    public List<EmployeePairResult> process(MultipartFile file) throws IOException {
        List<EmployeeProject> projects = this.fileReader.read(file);
        return this.pairProjectsProcessor.findTop(projects);
    }
}
