package com.app.farma.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private double price;

  @Column(nullable = false, length = 100)
  private String lab;

  @Column(nullable = false)
  private int quantity;

  @Builder.Default
  @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT TRUE")
  private boolean active = true;

  @Column(name = "expiration_date", nullable = false)
  private LocalDate expirationDate;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product" ,fetch = FetchType.LAZY)
  private List<Sale> sales = new ArrayList<>();


}
