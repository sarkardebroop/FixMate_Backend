package com.debroop.FixMate.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debroop.FixMate.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
