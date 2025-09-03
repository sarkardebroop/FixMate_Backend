package com.debroop.FixMate.domain;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "technician_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "user")
public class TechnicianProfile {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  // CSV for MVP, e.g., "plumbing,electrical"
  @Column(nullable = false)
  private String skills;

  @Column(name = "base_rate", precision = 10, scale = 2, nullable = false)
  private BigDecimal baseRate;

  @Column(name = "rating_avg", precision = 3, scale = 2, nullable = false)
  private BigDecimal ratingAvg;

  @Column(name = "jobs_completed", nullable = false)
  private Integer jobsCompleted;

  @Column(name = "service_radius_km", precision = 6, scale = 2, nullable = false)
  private BigDecimal serviceRadiusKm;

  @Column(precision = 9, scale = 6)  // NUMERIC(9,6)
  private BigDecimal lat;

  @Column(precision = 9, scale = 6)  // NUMERIC(9,6)
  private BigDecimal lng;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
  private Instant updatedAt;
}
