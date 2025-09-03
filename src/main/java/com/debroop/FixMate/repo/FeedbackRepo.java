package com.debroop.FixMate.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByJobId(Long jobId);
}
