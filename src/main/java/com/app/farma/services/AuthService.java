package com.app.farma.services;

import com.app.farma.dtos.AuthResponse;
import com.app.farma.dtos.LoginRequestDto;
import com.app.farma.dtos.RegisterRequestDto;
import com.app.farma.entities.Role;
import com.app.farma.entities.User;
import com.app.farma.repositories.RoleRepository;
import com.app.farma.repositories.UserRepository;
import com.app.farma.security.JwtService;
import com.app.farma.security.UserCustomDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public AuthResponse register(RegisterRequestDto dto){
    if(userRepository.existsByEmail(dto.email())){
      throw new RuntimeException("El email ya está registrado");
    }

    Role userRole = roleRepository.findByName("EMPLOYEE")
        .orElseThrow(() -> new RuntimeException("Rol employee no encontrado"));

    User user = User.builder()
        .name(dto.name())
        .lastName(dto.lastname())
        .email(dto.email())
        .password(passwordEncoder.encode(dto.password())) // hashear
        .roles(List.of(userRole))
        .build();
    this.userRepository.save(user);
    var userDetails = new UserCustomDetail(user);
    String token = this.jwtService.generateToken(userDetails);
    return  AuthResponse.builder()
        .token(token)
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }

  public AuthResponse login(LoginRequestDto request){

    var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
    this.authenticationManager.authenticate(authToken);

    User user = userRepository.findByEmail(request.email()).orElseThrow();

    var userDetails = new UserCustomDetail(user);
    String token = jwtService.generateToken(userDetails);

    return AuthResponse.builder()
        .token(token)
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }
}
