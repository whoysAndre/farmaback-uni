package com.app.farma.controllers;

import com.app.farma.dtos.SaleRequestDto;
import com.app.farma.dtos.SaleResponseDto;
import com.app.farma.entities.Sale;
import com.app.farma.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

  private final SaleService saleService;

  @PostMapping("/{productId}")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public ResponseEntity<SaleResponseDto> registerSale(
      @PathVariable UUID productId,
      @RequestBody SaleRequestDto dto
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.saleService.create(productId,dto.quantity()));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public ResponseEntity<List<SaleResponseDto>> getAllSales() {
    return ResponseEntity.ok(saleService.getAllSales());
  }



}
