package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class WithdrawFundsUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WithdrawFundsUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(String userID, BigDecimal amount) {
        UUID id = UUID.fromString(userID);
        Wallet wallet = this.walletRepository.findByUserId(id).orElseThrow(() -> new RuntimeException(
                "Wallet not found")
        );

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        this.walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .fromWallet(wallet)
                .toWallet(null)
                .amount(amount)
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .createdAt(LocalDateTime.now())
                .build();

        this.transactionRepository.save(transaction);
    }

}
