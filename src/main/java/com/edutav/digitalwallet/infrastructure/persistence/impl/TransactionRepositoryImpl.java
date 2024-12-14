package com.edutav.digitalwallet.infrastructure.persistence.impl;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.infrastructure.persistence.jpa.TransactionRepositoryJPA;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJPA transactionRepositoryJPA;

    public TransactionRepositoryImpl(TransactionRepositoryJPA transactionRepositoryJPA) {
        this.transactionRepositoryJPA = transactionRepositoryJPA;
    }

    @Override
    public void save(com.edutav.digitalwallet.domain.entities.Transaction transaction) {
        this.transactionRepositoryJPA.save(transaction);
    }

    @Override
    public List<Transaction> findByToWalletOrFromWalletAndCreatedAtBefore(
            Wallet toWallet, Wallet fromWallet, LocalDateTime date
    ) {
        return this.transactionRepositoryJPA.findByToWalletOrFromWalletAndCreatedAtBefore(toWallet, fromWallet, date);
    }


}
