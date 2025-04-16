package com.devteria.identityService.repositories;

import com.devteria.identityService.entities.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, String> {
}
