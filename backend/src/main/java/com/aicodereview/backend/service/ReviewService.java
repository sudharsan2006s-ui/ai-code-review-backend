package com.aicodereview.backend.service;

import com.aicodereview.backend.entity.Review;
import com.aicodereview.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    public String generateReview(String code) {

        StringBuilder review = new StringBuilder();

        review.append("========== AI CODE REVIEW REPORT ==========\n\n");

        int score = 100;

        // Exception Handling
        if (!code.contains("try")) {
            review.append("❌ Missing try-catch block.\n");
            score -= 10;
        } else {
            review.append("✅ Exception handling found.\n");
        }

        // Comments
        if (!code.contains("//") && !code.contains("/*")) {
            review.append("❌ No comments found.\n");
            score -= 5;
        } else {
            review.append("✅ Comments present.\n");
        }

        // System.out.println
        if (code.contains("System.out.println")) {
            review.append("⚠ Avoid using System.out.println() in production.\n");
            score -= 10;
        }

        // Long File
        if (code.length() > 1000) {
            review.append("⚠ Large source file. Consider splitting into multiple classes.\n");
            score -= 5;
        }

        // Main Method
        if (code.contains("public static void main")) {
            review.append("✅ Main method detected.\n");
        }

        // Class
        if (code.contains("class")) {
            review.append("✅ Java class found.\n");
        }

        // Loops
        if (code.contains("for(") || code.contains("while(")) {
            review.append("✅ Loop detected.\n");
        }

        // Naming
        review.append("✔ Follow Java naming conventions.\n");
        review.append("✔ Validate all user input.\n");
        review.append("✔ Use meaningful variable names.\n");
        review.append("✔ Follow SOLID principles.\n");

        review.append("\n=====================================\n");
        review.append("Quality Score : ").append(score).append("/100\n");

        if (score >= 90)
            review.append("Excellent Code ⭐⭐⭐⭐⭐");
        else if (score >= 75)
            review.append("Good Code ⭐⭐⭐⭐");
        else if (score >= 60)
            review.append("Needs Improvement ⭐⭐⭐");
        else
            review.append("Poor Quality ⭐⭐");

        return review.toString();
    }
    public int countLines(String code) {
        return code.split("\n").length;
    }

    public int countClasses(String code) {
        int count = 0;
        String[] words = code.split("\\s+");

        for (String word : words) {
            if (word.equals("class")) {
                count++;
            }
        }

        return count;
    }

    public int countMethods(String code) {

        int count = 0;

        String[] lines = code.split("\n");

        for (String line : lines) {

            line = line.trim();

            if ((line.contains("public") ||
                    line.contains("private") ||
                    line.contains("protected"))
                    && line.contains("(")
                    && line.contains(")")
                    && line.contains("{")) {

                count++;
            }
        }

        return count;
    }
    public List<Review> searchReviews(String fileName) {
        return reviewRepository.findByFileNameContainingIgnoreCase(fileName);
    }
    public Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow();
    }
}