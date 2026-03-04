package com.app.farma.services;

import com.app.farma.dtos.RoleRequestDto;
import com.app.farma.entities.Role;
import com.app.farma.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public List<Role> findAll(){
    return this.roleRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Role findById(UUID id){
    return this.roleRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Role not found"));
  }

  @Transactional
  public Role create(RoleRequestDto dto){
    boolean roleNamExist = this.roleRepository.existsByName(dto.name());
    if(roleNamExist){
      throw new RuntimeException("Rol already exist");
    }
    Role role = Role.builder()
        .name(dto.name())
        .build();
    Role saved = this.roleRepository.save(role);
    return role;
  }

}

