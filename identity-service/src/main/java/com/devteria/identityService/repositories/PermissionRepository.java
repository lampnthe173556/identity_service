package com.devteria.identityService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devteria.identityService.entities.Permissions;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, String> {}
