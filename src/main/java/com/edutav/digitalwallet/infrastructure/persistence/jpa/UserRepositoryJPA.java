package com.edutav.digitalwallet.infrastructure.persistence.jpa;

import com.edutav.digitalwallet.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryJPA extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
