package com.app.farma.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

  private UUID id;
  private String name;
  private double price;
  private int quantity;
  private String lab;
  private LocalDate expirationDate;
  private boolean active;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID categoryId;
  private String categoryName;

}
