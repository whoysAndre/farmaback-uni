package com.app.farma.controllers;

import com.app.farma.dtos.CategoryRequestDto;
import com.app.farma.entities.Category;
import com.app.farma.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Category>> findAll(){
    return  ResponseEntity.ok(this.categoryService.findAll());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<Category> create(@RequestBody CategoryRequestDto categoryRequestDto){
    return  ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.create(categoryRequestDto));
  }


}
