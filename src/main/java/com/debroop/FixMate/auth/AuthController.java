package com.debroop.FixMate.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debroop.FixMate.auth.dto.AuthResponse;
import com.debroop.FixMate.auth.dto.LoginRequest;
import com.debroop.FixMate.auth.dto.RegisterRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    String token = authService.register(req);
    return ResponseEntity.status(201).body(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
    try {
      String token = authService.login(req);
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(401).build();
    }
  }
  
}
