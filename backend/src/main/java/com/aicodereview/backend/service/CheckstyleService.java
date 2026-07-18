package com.aicodereview.backend.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckstyleService {

    public String analyze(String code) {

        List<String> issues = new ArrayList<>();

        String[] lines = code.split("\n");

        for (int i = 0; i < lines.length; i++) {

            String line = lines[i];

            if (line.length() > 100) {
                issues.add("Line " + (i + 1) + ": Line exceeds 100 characters.");
            }

            if (line.contains("\t")) {
                issues.add("Line " + (i + 1) + ": Tab character found. Use spaces.");
            }

            if (line.contains("System.out.println")) {
                issues.add("Line " + (i + 1) + ": Avoid using System.out.println().");
            }

            if (line.trim().startsWith("public class") &&
                    !line.contains("{")) {
                issues.add("Line " + (i + 1) + ": Opening brace should be on same line.");
            }
        }

        if (issues.isEmpty()) {
            return "No Checkstyle violations found.";
        }

        return String.join("\n", issues);
    }
}