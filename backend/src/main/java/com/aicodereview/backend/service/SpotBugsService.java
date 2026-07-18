package com.aicodereview.backend.service;

import org.springframework.stereotype.Service;

@Service
public class SpotBugsService {

    public String analyze(String code) {

        StringBuilder report = new StringBuilder();

        if (code.contains("== null")) {
            report.append("Check for possible NullPointerException.\n");
        }

        if (code.contains("new Random()")) {
            report.append("Consider reusing Random instance instead of creating multiple objects.\n");
        }

        if (code.contains("System.gc()")) {
            report.append("Avoid calling System.gc() directly.\n");
        }

        if (code.contains("Thread.sleep")) {
            report.append("Thread.sleep() may affect application performance.\n");
        }

        if (report.length() == 0) {
            report.append("No SpotBugs issues found.");
        }

        return report.toString();
    }
}