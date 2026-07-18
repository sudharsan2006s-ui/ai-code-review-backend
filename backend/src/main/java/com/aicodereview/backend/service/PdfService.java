package com.aicodereview.backend.service;

import com.aicodereview.backend.entity.Review;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(Review review) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("AI Code Review Report"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("File Name: " + review.getFileName()));
        document.add(new Paragraph("Quality Score: " + review.getQualityScore()));
        document.add(new Paragraph("Total Lines: " + review.getTotalLines()));
        document.add(new Paragraph("Methods: " + review.getMethodsCount()));
        document.add(new Paragraph("Classes: " + review.getClassCount()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("AI Review"));
        document.add(new Paragraph(review.getReviewResult()));

        document.close();

        return output.toByteArray();
    }
}