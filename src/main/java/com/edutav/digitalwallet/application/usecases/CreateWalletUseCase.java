package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.application.dtos.WalletInputDTO;
import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.User;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.UserRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateWalletUseCase {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public CreateWalletUseCase(
            WalletRepository walletRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(String userID, BigDecimal initialBalance) {
        UUID id = UUID.fromString(userID);
        Wallet wallet = new Wallet();
        User user = this.userRepository.findById(id).orElseThrow();
        wallet.setUser(user);
        wallet.setBalance(initialBalance);
        this.walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .toWallet(wallet)
                .fromWallet(null)
                .amount(initialBalance)
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        this.transactionRepository.save(transaction);
    }
}
