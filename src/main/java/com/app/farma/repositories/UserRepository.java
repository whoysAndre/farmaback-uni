package com.app.farma.repositories;

import com.app.farma.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  // Buscar usuario por email (útil para login)
  Optional<User> findByEmail(String email);

  // Verificar si existe un email
  boolean existsByEmail(String email);


}



