package com.app.farma.repositories;

import com.app.farma.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
  boolean existsByName(String name);
}
