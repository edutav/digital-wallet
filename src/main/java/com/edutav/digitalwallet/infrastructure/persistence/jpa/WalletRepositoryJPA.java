package com.edutav.digitalwallet.infrastructure.persistence.jpa;

import com.edutav.digitalwallet.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryJPA extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUser_Id(UUID userId);
}
