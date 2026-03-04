package com.app.farma.security;

import com.app.farma.entities.User;
import com.app.farma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user= this.userRepository.findByEmail(email).orElseThrow(()->
        new UsernameNotFoundException("Usuario no encontrado con email: " + email)
    );
    return  new UserCustomDetail(user);
  }

}
