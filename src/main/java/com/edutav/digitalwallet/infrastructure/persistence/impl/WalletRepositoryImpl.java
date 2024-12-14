package com.edutav.digitalwallet.infrastructure.persistence.impl;

import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;
import com.edutav.digitalwallet.infrastructure.persistence.jpa.WalletRepositoryJPA;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletRepositoryJPA walletRepositoryJPA;

    public WalletRepositoryImpl(WalletRepositoryJPA walletRepositoryJPA) {
        this.walletRepositoryJPA = walletRepositoryJPA;
    }

    @Override
    public void save(Wallet wallet) {
        this.walletRepositoryJPA.save(wallet);
    }

    @Override
    public Optional<Wallet> findByUserId(UUID userId) {
        return this.walletRepositoryJPA.findByUser_Id(userId);
    }
}
