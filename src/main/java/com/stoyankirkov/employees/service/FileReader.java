package com.stoyankirkov.employees.service;

import com.stoyankirkov.employees.model.EmployeeProject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileReader {

    List<EmployeeProject> read(MultipartFile file) throws IOException;
}
