package com.aicodereview.backend.service;

import org.springframework.stereotype.Service;

@Service
public class PmdService {

    public String analyze(String code) {

        StringBuilder report = new StringBuilder();

        if (code.contains("catch(Exception")) {
            report.append("Avoid catching generic Exception.\n");
        }

        if (code.contains("==") && code.contains("String")) {
            report.append("Use .equals() instead of == for String comparison.\n");
        }

        if (code.length() > 2000) {
            report.append("Large class detected.\n");
        }

        if (code.contains("public static")) {
            report.append("Too many static methods may reduce maintainability.\n");
        }

        if (report.length() == 0) {
            report.append("No PMD violations found.");
        }

        return report.toString();
    }
}