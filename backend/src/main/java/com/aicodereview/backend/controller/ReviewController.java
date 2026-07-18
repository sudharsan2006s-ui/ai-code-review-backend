package com.aicodereview.backend.controller;

import com.aicodereview.backend.entity.Review;
import com.aicodereview.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.aicodereview.backend.service.GeminiService;
import com.aicodereview.backend.service.ReviewService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private CheckstyleService checkstyleService;
    @Autowired
    private PmdService pmdService;
    @Autowired
    private SpotBugsService spotBugsService;
    @Autowired
    private GeminiService geminiService;

    @PostMapping("/upload")
    public Review uploadFile(@RequestParam("file") MultipartFile file) {

        try {

            String code = new String(file.getBytes());

            String review;

            try {
                review = geminiService.generateReview(code);
            } catch (Exception e) {
                e.printStackTrace();
                review = reviewService.generateReview(code);
            }
            String checkstyle = checkstyleService.analyze(code);
            String pmd = pmdService.analyze(code);
            String spotbugs = spotBugsService.analyze(code);

            Review result = new Review();

            result.setFileName(file.getOriginalFilename());
            result.setCode(code);

            result.setReviewResult(review);

            result.setTotalLines(reviewService.countLines(code));
            result.setMethodsCount(reviewService.countMethods(code));
            result.setClassCount(reviewService.countClasses(code));

            int score = 100;

            if (!code.contains("try"))
                score -= 10;

            if (!code.contains("//"))
                score -= 5;

            if (code.contains("System.out.println"))
                score -= 10;

            result.setQualityScore(score);
            result.setCheckstyleReport(checkstyle);
            result.setPmdReport(pmd);
            result.setSpotbugsReport(spotbugs);

            return reviewService.saveReview(result);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "Review deleted successfully";
    }
    @GetMapping("/search")
    public List<Review> searchReviews(@RequestParam String fileName) {
        return reviewService.searchReviews(fileName);
    }
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {

        Review review = reviewService.getReview(id);

        byte[] pdf = pdfService.generatePdf(review);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=Review_Report.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }
    @PostMapping("/paste")
    public Review reviewCode(@RequestBody Map<String, String> request) {

        try {

            String code = request.get("code");

// Validate pasted code
            if (code == null || code.trim().isEmpty()) {
                throw new RuntimeException("Please paste Java code.");
            }

            if (!(code.contains("class")
                    || code.contains("public")
                    || code.contains("void")
                    || code.contains("import"))) {
                throw new RuntimeException("Invalid Java code. Please paste a valid Java program.");
            }

            String review;

            try {
                review = geminiService.generateReview(code);
            } catch (Exception e) {
                e.printStackTrace();
                review = reviewService.generateReview(code);
            }

            String checkstyle = checkstyleService.analyze(code);
            String pmd = pmdService.analyze(code);
            String spotbugs = spotBugsService.analyze(code);

            Review result = new Review();

            result.setFileName("Pasted Code");
            result.setCode(code);
            result.setReviewResult(review);

            result.setTotalLines(reviewService.countLines(code));
            result.setMethodsCount(reviewService.countMethods(code));
            result.setClassCount(reviewService.countClasses(code));

            int score = 100;

            if (!code.contains("try"))
                score -= 10;

            if (!code.contains("//"))
                score -= 5;

            if (code.contains("System.out.println"))
                score -= 10;

            result.setQualityScore(score);

            result.setCheckstyleReport(checkstyle);
            result.setPmdReport(pmd);
            result.setSpotbugsReport(spotbugs);

            return reviewService.saveReview(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}