package com.app.farma.services;

import com.app.farma.dtos.SaleResponseDto;
import com.app.farma.entities.Product;
import com.app.farma.entities.Sale;
import com.app.farma.repositories.ProductRepository;
import com.app.farma.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService   {

  private final ProductRepository productRepository;
  private final SaleRepository saleRepository;

  @Transactional
  public SaleResponseDto create(UUID productId, int quantitySold){

    Product product = this.productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    if(!product.isActive()){
      throw new RuntimeException("El producto no está disponible");
    }

    if (product.getQuantity() < quantitySold) {
      throw new RuntimeException("Stock insuficiente. Disponible: " + product.getQuantity());
    }

    product.setQuantity(product.getQuantity() - quantitySold);
    this.productRepository.save(product);

    Sale sale = Sale.builder()
        .product(product)
        .quantity(quantitySold)
        .unitPrice(product.getPrice())
        .totalPrice(product.getPrice() * quantitySold)
        .build();

    Sale saved = this.saleRepository.save(sale);
    return SaleResponseDto.builder()
        .id(saved.getId())
        .quantity(saved.getQuantity())
        .unitPrice(saved.getUnitPrice())
        .totalPrice(saved.getTotalPrice())
        .productName(saved.getProduct().getName())
        .productPrice(saved.getProduct().getPrice())
        .productPrice(saved.getProduct().getQuantity())
        .build();
  }

  @Transactional(readOnly = true)
  public List<SaleResponseDto> getAllSales(){
    return this.saleRepository.findAllWithProduct().stream().map(sale->{
      return SaleResponseDto.builder()
          .id(sale.getId())
          .quantity(sale.getQuantity())
          .unitPrice(sale.getUnitPrice())
          .totalPrice(sale.getTotalPrice())
          .productName(sale.getProduct().getName())
          .productPrice(sale.getProduct().getPrice())
          .productPrice(sale.getProduct().getQuantity())
          .soldAt(sale.getSoldAt())
          .build();
    }).toList();
  }

}
