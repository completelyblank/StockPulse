package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.Transaction;
import com.stockpulse.stockpulse.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository txnRepo;

    @GetMapping("/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable UUID userId) {
        return txnRepo.findByUserIdOrderByTimestampDesc(userId);
    }
}
