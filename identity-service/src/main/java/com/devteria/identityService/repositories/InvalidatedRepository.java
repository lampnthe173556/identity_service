package com.devteria.identityService.repositories;

import com.devteria.identityService.entities.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {
}
