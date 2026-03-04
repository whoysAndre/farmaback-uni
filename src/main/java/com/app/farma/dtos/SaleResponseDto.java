package com.app.farma.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDto {

  private UUID id;
  private int quantity;
  private double unitPrice;
  private double totalPrice;

  private String productName;
  private double productPrice;
  private int productQuantityRest;

  private LocalDateTime soldAt;

}
