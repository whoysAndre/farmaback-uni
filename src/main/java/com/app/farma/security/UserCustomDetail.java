package com.app.farma.security;

import com.app.farma.entities.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class UserCustomDetail implements UserDetails {


  private final UUID id;
  private String name;
  private String lastname;
  private final String email;
  private final String password;  // ← el hash BCrypt de la BD
  private final Collection<? extends GrantedAuthority> authorities;

  public UserCustomDetail(User user){
    this.id        = user.getId();
    this.name      = user.getName();
    this.lastname  = user.getLastName();
    this.email     = user.getEmail();
    this.password  = user.getPassword();
    this.authorities = user.getRoles().stream()
        .map(role-> new SimpleGrantedAuthority("ROLE_"+role.getName()))
        .collect(Collectors.toList());
  }


  @Override
  public String getUsername() {
    return this.email; // usamos email como "username"
  }

  @Override
  public String getPassword() {
    return this.password; // el hash BCrypt
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities; // los roles mapeados
  }

  // Estado de la cuenta — todos true por ahora
  @Override public boolean isAccountNonExpired()    { return true; }
  @Override public boolean isAccountNonLocked()     { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled()               { return true; }

  // ── Getters extra (datos que usarás en el Controller) ──
  public UUID   getId()       { return id; }
  public String getName()     { return name; }
  public String getLastName() { return lastname; }
  public String getEmail()    { return email; }
}
