package com.devteria.identityService.repositories;

import com.devteria.identityService.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {
}
