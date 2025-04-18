package com.devteria.identityService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devteria.identityService.entities.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {}
