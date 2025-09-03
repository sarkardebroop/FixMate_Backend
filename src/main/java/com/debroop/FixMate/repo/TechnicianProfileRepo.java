package com.debroop.FixMate.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.TechnicianProfile;

public interface TechnicianProfileRepo extends JpaRepository<TechnicianProfile, Long> {
  Optional<TechnicianProfile> findByUserId(Long userId);
}
