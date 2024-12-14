package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DepositFundsUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public DepositFundsUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(String userID, BigDecimal amount) {
        UUID id = UUID.fromString(userID);
        Wallet wallet = walletRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .toWallet(wallet)
                .fromWallet(null)
                .amount(amount)
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

}
