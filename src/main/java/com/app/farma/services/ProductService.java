package com.app.farma.services;

import com.app.farma.dtos.ProductRequestDto;
import com.app.farma.dtos.ProductResponseDto;
import com.app.farma.entities.Category;
import com.app.farma.entities.Product;
import com.app.farma.repositories.CategoryRepository;
import com.app.farma.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<ProductResponseDto> findAll(){

    return  this.productRepository.findByActiveTrue().stream().map(product->{
      return  new ProductResponseDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getQuantity(),
        product.getLab(),
        product.getExpirationDate(),
        product.isActive(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        product.getCategory().getId(),
        product.getCategory().getName()
      );
    }).toList();
  }

  @Transactional
  public ProductResponseDto create(ProductRequestDto dto){

    if (productRepository.existsByName(dto.getName())) {
      throw new RuntimeException("Ya existe un producto con ese nombre");
    }

    Category category = this.categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

    Product product = Product.builder()
        .name(dto.getName())
        .price(dto.getPrice())
        .lab(dto.getLab())
        .quantity(dto.getQuantity())
        .expirationDate(dto.getExpirationDate())
        .category(category)
        .build();

    Product saved = this.productRepository.save(product);

    return ProductResponseDto.builder()
        .id(saved.getId())
        .name(saved.getName())
        .price(saved.getPrice())
        .lab(saved.getLab())
        .quantity(saved.getQuantity())
        .expirationDate(saved.getExpirationDate())
        .active(saved.isActive())
        .categoryId(saved.getCategory().getId())
        .categoryName(saved.getCategory().getName())
        .createdAt(saved.getCreatedAt())
        .updatedAt(saved.getUpdatedAt())
        .build();
  }

  @Transactional(readOnly = true)
  public ProductResponseDto findById(UUID id){
    Product product = this.productRepository.findById(id).orElseThrow(()-> new RuntimeException("No existe el producto"));
    return new ProductResponseDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getQuantity(),
        product.getLab(),
        product.getExpirationDate(),
        product.isActive(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        product.getCategory().getId(),
        product.getCategory().getName());
  }

  @Transactional
  public Map<String,Object> update(UUID id, ProductRequestDto dto){

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

    product.setName(dto.getName());
    product.setPrice(dto.getPrice());
    product.setLab(dto.getLab());
    product.setQuantity(dto.getQuantity());
    product.setExpirationDate(dto.getExpirationDate());
    product.setCategory(category);

    this.productRepository.save(product);

    Map<String,Object> json = new HashMap<>();
    json.put("status",true);
    json.put("message", "Producto actualizado correctamente");
    return json;
  }

  @Transactional
  public Map<String,Object> delete(UUID id){

    Product product = this.productRepository.findById(id).orElseThrow(()-> new RuntimeException("No existe el producto"));
    product.setActive(false);
    this.productRepository.save(product);
    Map<String,Object> json = new HashMap<>();
    json.put("status",true);
    json.put("message", "Producto eliminado correctamente");
    return json;
  }



}
