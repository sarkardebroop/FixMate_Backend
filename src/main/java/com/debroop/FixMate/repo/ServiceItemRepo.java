package com.debroop.FixMate.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.ServiceItem;

public interface ServiceItemRepo extends JpaRepository<ServiceItem, Long> {
  List<ServiceItem> findByCategoryId(Long categoryId);
  List<ServiceItem> findByActiveTrue();
}
