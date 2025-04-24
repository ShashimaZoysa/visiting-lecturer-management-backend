package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);  // To fetch roles by name
}
