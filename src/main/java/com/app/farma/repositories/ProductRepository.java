package com.app.farma.repositories;

import com.app.farma.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findByName(String name);
  List<Product> findByActiveTrue();
  boolean existsByName(String name);
}
