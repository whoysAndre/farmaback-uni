package com.app.farma.repositories;

import com.app.farma.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

  @Query("SELECT s FROM Sale s JOIN FETCH s.product")
  List<Sale> findAllWithProduct();

}
