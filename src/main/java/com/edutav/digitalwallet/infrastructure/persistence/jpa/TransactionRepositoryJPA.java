package com.edutav.digitalwallet.infrastructure.persistence.jpa;

import com.edutav.digitalwallet.domain.entities.Transaction;
import com.edutav.digitalwallet.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepositoryJPA extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByToWalletOrFromWalletAndCreatedAtBefore(
            Wallet toWallet, Wallet fromWallet, LocalDateTime createdAtBefore
    );
}
