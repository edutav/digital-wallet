package com.edutav.digitalwallet.infrastructure.persistence.impl;

import com.edutav.digitalwallet.domain.entities.User;
import com.edutav.digitalwallet.domain.repositories.UserRepository;
import com.edutav.digitalwallet.infrastructure.persistence.jpa.UserRepositoryJPA;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJPA userRepositoryJPA;

    public UserRepositoryImpl(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public void save(User user) {
        this.userRepositoryJPA.save(user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return this.userRepositoryJPA.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepositoryJPA.findByUsername(username);
    }
}
