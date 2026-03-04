package com.app.farma.controllers;

import com.app.farma.dtos.RoleRequestDto;
import com.app.farma.entities.Role;
import com.app.farma.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  @GetMapping
  public ResponseEntity<List<Role>> findAll(){
    return ResponseEntity.ok(this.roleService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Role> findById(@PathVariable UUID id){
    return  ResponseEntity.ok(this.roleService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Role> create(@RequestBody RoleRequestDto dto){
    return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(dto));
  }

}
