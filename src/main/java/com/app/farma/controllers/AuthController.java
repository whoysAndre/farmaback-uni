package com.app.farma.controllers;

import com.app.farma.dtos.AuthResponse;
import com.app.farma.dtos.LoginRequestDto;
import com.app.farma.dtos.RegisterRequestDto;
import com.app.farma.security.UserCustomDetail;
import com.app.farma.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private  final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDto dto){
    return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(dto));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto dto){
    return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.login(dto));
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String> me(@AuthenticationPrincipal UserCustomDetail userCustomDetail){
    return  ResponseEntity.ok("Hola" + userCustomDetail.getId());
  }

}
