package com.app.farma.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private double unitPrice;

  @Column(nullable = false)
  private double totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @CreationTimestamp
  @Column(name = "sold_at", updatable = false)
  private LocalDateTime soldAt;

}
