package com.stoyankirkov.employees.controller;

import com.stoyankirkov.employees.model.EmployeePairResult;
import com.stoyankirkov.employees.service.FileProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsvController {

    private final FileProcessor fileProcessor;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        log.info("CSV file uploaded for processing.");
        if (file.isEmpty()) {
            model.addAttribute("error", "File is not chosen!");
            return "index";
        }

        List<EmployeePairResult> results = this.fileProcessor.process(file);
        model.addAttribute("results", results);
        log.info("CSV file processed.");
        return "index";
    }
}
