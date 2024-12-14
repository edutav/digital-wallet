package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransferFundsUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransferFundsUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(String fromUserID, String toUserID, BigDecimal amount) {
        UUID fromId = UUID.fromString(fromUserID);
        UUID toId = UUID.fromString(toUserID);

        Wallet fromWallet = this.walletRepository.findByUserId(fromId).orElseThrow(
                () -> new RuntimeException("Sender's wallet not found")
        );

        Wallet toWallet = this.walletRepository.findByUserId(toId).orElseThrow(
                () -> new RuntimeException("Receiver's wallet not found")
        );

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        this.walletRepository.save(fromWallet);
        this.walletRepository.save(toWallet);

        Transaction transaction = Transaction.builder()
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(amount)
                .transactionType(Transaction.TransactionType.TRANSFER)
                .createdAt(LocalDateTime.now())
                .build();

        this.transactionRepository.save(transaction);
    }

}
