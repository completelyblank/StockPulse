package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockHoldingRepository extends JpaRepository<StockHolding, UUID> {
    List<StockHolding> findByUser(User user);
    StockHolding findByUserAndStockSymbol(User user, String symbol);
}
