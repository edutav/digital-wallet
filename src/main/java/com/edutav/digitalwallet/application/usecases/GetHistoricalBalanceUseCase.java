package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GetHistoricalBalanceUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public GetHistoricalBalanceUseCase(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal execute(String userID, LocalDateTime date) {
        UUID id = UUID.fromString(userID);

        Wallet wallet = this.walletRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        List<Transaction> transactions = this.transactionRepository.findByToWalletOrFromWalletAndCreatedAtBefore(
                wallet, wallet, date
        );

        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getToWallet().equals(wallet)) {
                balance = balance.add(transaction.getAmount());
            } else if (transaction.getFromWallet().equals(wallet)) {
                balance = balance.subtract(transaction.getAmount());
            }
        }

        return balance;
    }

}
