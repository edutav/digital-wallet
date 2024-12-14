package com.edutav.digitalwallet.domain.repositories;

import com.edutav.digitalwallet.domain.entities.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    void save(Wallet wallet);
    Optional<Wallet> findByUserId(UUID userId);
}
