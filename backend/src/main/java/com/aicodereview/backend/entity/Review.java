package com.aicodereview.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column(columnDefinition = "TEXT")
    private String reviewResult;

    private Integer qualityScore;

    private Integer totalLines;

    private Integer methodsCount;

    private Integer classCount;

    @Column(columnDefinition = "TEXT")
    private String checkstyleReport;

    @Column(columnDefinition = "TEXT")
    private String pmdReport;

    @Column(columnDefinition = "TEXT")
    private String spotbugsReport;
}