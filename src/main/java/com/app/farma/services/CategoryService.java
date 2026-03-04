package com.app.farma.services;

import com.app.farma.dtos.CategoryRequestDto;
import com.app.farma.entities.Category;
import com.app.farma.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;


  public List<Category> findAll(){
    return this.categoryRepository.findAll();
  }


  public Category findById(UUID id){
    return  this.categoryRepository.findById(id).orElseThrow();
  }

  public Category create(CategoryRequestDto dto){

    if(this.categoryRepository.existsByName(dto.name())){
      throw new RuntimeException("Ya existe una categoria con ese nombre");
    }
    Category category = Category.builder()
        .name(dto.name())
        .build();
    return  this.categoryRepository.save(category);
  }

}
