package com.debroop.FixMate.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.Job;
import com.debroop.FixMate.domain.JobStatus;

public interface JobRepo extends JpaRepository<Job, Long> {
  List<Job> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
  List<Job> findByTechnicianIdOrderByCreatedAtDesc(Long technicianId);
  List<Job> findByStatus(JobStatus status);
}
