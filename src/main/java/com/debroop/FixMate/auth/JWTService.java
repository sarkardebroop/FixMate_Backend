package com.debroop.FixMate.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

  private final Key key;
  private final long expiryMs;

  public JWTService(
      @Value("${app.security.jwt-secret}") String secret,
      @Value("${app.security.jwt-expiration-minutes}") long expiryMinutes
  ) {
      // HS256 expects a key of at least 32 bytes (256 bits)
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      if (keyBytes.length < 32) {
        throw new IllegalArgumentException("JWT secret key must be at least 32 bytes (256 bits) long. Current length: " + keyBytes.length + ". Please set a longer value for app.security.jwt-secret in your application.yml or properties.");
      }
      this.key = Keys.hmacShaKeyFor(keyBytes);
      this.expiryMs = expiryMinutes * 60_000L;
  }

  public String generate(String subject, Map<String, Object> claims) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(subject)
        .addClaims(claims)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + expiryMs))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
    public boolean isValid(String token, UserDetails user) {
        var claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return user.getUsername().equals(claims.getSubject()) && claims.getExpiration().after(new java.util.Date());
    }

  public Jws<Claims> parse(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
  }
  public String getSubject(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
  }


}
