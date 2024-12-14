package com.edutav.digitalwallet.domain.repositories;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction wallet);
    List<Transaction> findByToWalletOrFromWalletAndCreatedAtBefore(
            Wallet toWallet, Wallet fromWallet, LocalDateTime date
    );
}
