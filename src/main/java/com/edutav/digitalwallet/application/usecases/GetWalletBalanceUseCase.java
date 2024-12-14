package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class GetWalletBalanceUseCase {

    private final WalletRepository walletRepository;

    public GetWalletBalanceUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public BigDecimal execute(String userId) {
        UUID id = UUID.fromString(userId);
        return this.walletRepository.findByUserId(id)
                .map(wallet -> wallet.getBalance())
                .orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));
    }

}
