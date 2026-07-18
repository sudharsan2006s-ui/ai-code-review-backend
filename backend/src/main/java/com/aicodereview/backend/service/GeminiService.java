package com.aicodereview.backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateReview(String code) {

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt = """
You are a senior Java code reviewer.

Review the following Java code.

Give:
1. Code Quality
2. Bugs
3. Performance
4. Security
5. Best Practices
6. Final Score out of 100

Java Code:

""" + code;

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.0-flash",
                        prompt,
                        null);

        return response.text();
    }
}