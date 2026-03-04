package com.app.farma.dtos;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductRequestDto{

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
  private String name;

  @NotNull(message = "El precio es obligatorio")
  @Positive(message = "El precio debe ser positivo")
  private double price;

  @NotNull
  @Min(value = 0, message = "El cantidad no puede ser negativo")
  private int quantity;

  @NotBlank(message = "El laboratorio es obligatorio")
  @Size(max = 200, message = "El laboratorio no puede superar 200 caracteres")
  private String lab;

  @NotNull(message = "La fecha de vencimiento en obligatorio")
  private LocalDate expirationDate;

  @NotNull(message = "Debes indicar la categoría")
  private UUID categoryId;


}
