package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.*;
import com.stockpulse.stockpulse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TradeService {

    @Autowired private UserRepository userRepo;
    @Autowired private StockRepository stockRepo;
    @Autowired private StockHoldingRepository holdingRepo;

    public String buyStock(String username, String symbol, int quantity) {
        User user = userRepo.findByUsername(username).orElseThrow();
        Stock stock = stockRepo.findById(symbol).orElseThrow();

        double cost = quantity * stock.getPrice();
        if (user.getBalance() < cost) {
            return "Insufficient balance.";
        }

        user.setBalance(user.getBalance() - cost);
        StockHolding holding = holdingRepo.findByUserAndStockSymbol(user, symbol);
        if (holding == null) {
            holding = new StockHolding(null, symbol, quantity, user);
        } else {
            holding.setQuantity(holding.getQuantity() + quantity);
        }

        holdingRepo.save(holding);
        userRepo.save(user);
        return "Bought " + quantity + " of " + symbol;
    }

    public String sellStock(String username, String symbol, int quantity) {
        User user = userRepo.findByUsername(username).orElseThrow();
        Stock stock = stockRepo.findById(symbol).orElseThrow();

        StockHolding holding = holdingRepo.findByUserAndStockSymbol(user, symbol);
        if (holding == null || holding.getQuantity() < quantity) {
            return "Not enough shares.";
        }

        double earnings = quantity * stock.getPrice();
        user.setBalance(user.getBalance() + earnings);
        holding.setQuantity(holding.getQuantity() - quantity);

        if (holding.getQuantity() == 0) {
            holdingRepo.delete(holding);
        } else {
            holdingRepo.save(holding);
        }

        userRepo.save(user);
        return "Sold " + quantity + " of " + symbol;
    }
}
