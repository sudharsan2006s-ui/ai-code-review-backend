package com.aicodereview.backend.repository;

import com.aicodereview.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFileNameContainingIgnoreCase(String fileName);

}