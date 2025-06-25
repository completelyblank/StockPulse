package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserIdOrderByTimestampDesc(UUID userId);
}
