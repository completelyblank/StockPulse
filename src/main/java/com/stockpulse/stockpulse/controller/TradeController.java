package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.dto.TradeRequest;
import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.model.Transaction;
import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.repository.TransactionRepository;
import com.stockpulse.stockpulse.repository.UserRepository;
import com.stockpulse.stockpulse.service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired private UserRepository userRepo;
    @Autowired private StockService stockService;
    @Autowired private TransactionRepository txnRepo;

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody TradeRequest request) {
        try {
            UUID userId = request.getUserId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            double stockPrice = stockService.getPriceForSymbol(request.getSymbol());
            if (stockPrice <= 0.0) {
                return ResponseEntity.badRequest().body("Unknown or invalid stock symbol: " + request.getSymbol());
            }

            double totalCost = stockPrice * request.getQuantity();

            if (user.getBalance() < totalCost) {
                return ResponseEntity.badRequest().body("Insufficient balance");
            }

            user.setBalance(user.getBalance() - totalCost);

            // Update or create holding
            boolean updated = false;
            for (StockHolding h : user.getHoldings()) {
                if (h.getStockSymbol().equalsIgnoreCase(request.getSymbol())) {
                    h.setQuantity(h.getQuantity() + request.getQuantity());
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                StockHolding newHolding = new StockHolding();
                newHolding.setStockSymbol(request.getSymbol());
                newHolding.setQuantity(request.getQuantity());
                newHolding.setUser(user);
                user.getHoldings().add(newHolding);
            }

            userRepo.save(user);

            // Save transaction
            Transaction txn = new Transaction();
            txn.setUserId(user.getId());
            txn.setStockSymbol(request.getSymbol());
            txn.setQuantity(request.getQuantity());
            txn.setPricePerUnit(stockPrice);
            txn.setType("BUY");
            txn.setTimestamp(LocalDateTime.now());
            txnRepo.save(txn);

            return ResponseEntity.ok(Map.of("message", "Stock purchased successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@RequestBody TradeRequest request) {
        try {
            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<StockHolding> userHoldings = user.getHoldings();
            StockHolding holding = null;

            for (StockHolding h : userHoldings) {
                if (h.getStockSymbol().equalsIgnoreCase(request.getSymbol())) {
                    holding = h;
                    break;
                }
            }

            if (holding == null || holding.getQuantity() < request.getQuantity()) {
                return ResponseEntity.badRequest().body("Insufficient shares to sell.");
            }

            double stockPrice = stockService.getPriceForSymbol(request.getSymbol());
            if (stockPrice <= 0.0) {
                return ResponseEntity.badRequest().body("Unknown or invalid stock symbol: " + request.getSymbol());
            }

            double totalRevenue = stockPrice * request.getQuantity();

            if (holding.getQuantity() == request.getQuantity()) {
                userHoldings.remove(holding);
            } else {
                holding.setQuantity(holding.getQuantity() - request.getQuantity());
            }

            user.setBalance(user.getBalance() + totalRevenue);
            userRepo.save(user);

            // Save transaction
            Transaction txn = new Transaction();
            txn.setUserId(user.getId());
            txn.setStockSymbol(request.getSymbol());
            txn.setQuantity(request.getQuantity());
            txn.setPricePerUnit(stockPrice);
            txn.setType("SELL");
            txn.setTimestamp(LocalDateTime.now());
            txnRepo.save(txn);

            return ResponseEntity.ok(Map.of("message", "Stock sold for $" + totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Internal server error: " + e.getMessage());
        }
    }
}
