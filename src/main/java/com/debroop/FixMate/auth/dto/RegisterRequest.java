package com.debroop.FixMate.auth.dto;

public record RegisterRequest(
    String email,
    String password,
    String fullName,
    String role   // CUSTOMER | TECHNICIAN | ADMIN
) {}
