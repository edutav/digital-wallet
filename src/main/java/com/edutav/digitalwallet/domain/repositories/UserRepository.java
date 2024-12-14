package com.edutav.digitalwallet.domain.repositories;

import com.edutav.digitalwallet.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UUID userId);
    Optional<User> findByUsername(String username);
}
