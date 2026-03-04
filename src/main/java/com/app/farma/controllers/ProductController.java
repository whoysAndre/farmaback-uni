package com.app.farma.controllers;

import com.app.farma.dtos.ProductRequestDto;
import com.app.farma.dtos.ProductResponseDto;
import com.app.farma.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
  public ResponseEntity<List<ProductResponseDto>> findAll(){
    return  ResponseEntity.ok(this.productService.findAll());
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid  ProductRequestDto dto){
    return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.create(dto));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
  public ResponseEntity<ProductResponseDto> findById(@PathVariable UUID id){
    return  ResponseEntity.ok(this.productService.findById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String,Object>> update(@PathVariable UUID id, @Valid @RequestBody ProductRequestDto dto){
    System.out.println("🔍 ID recibido: " + id);  // 👈 Ver qué llega
    System.out.println("🔍 DTO recibido: " + dto);
    return  ResponseEntity.status(HttpStatus.CREATED).body(this.productService.update(id,dto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
  public ResponseEntity<Map<String,Object>> delete(@PathVariable UUID id){
    return  ResponseEntity.ok(this.productService.delete(id));
  }

}
