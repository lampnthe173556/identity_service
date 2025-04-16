package com.devteria.identityService.repositories;

import com.devteria.identityService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findUserByUsername(String username);
    User getUserByUsername(String username);
}
