package com.debroop.FixMate.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.ServiceCategory;

public interface ServiceCategoryRepo extends JpaRepository<ServiceCategory, Long> {
  Optional<ServiceCategory> findByName(String name);
}
