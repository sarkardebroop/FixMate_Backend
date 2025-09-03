package com.debroop.FixMate.auth.dto;

public record LoginRequest(
    String email,
    String password
) {}
