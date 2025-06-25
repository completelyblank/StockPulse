package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.repository.StockHoldingRepository;
import com.stockpulse.stockpulse.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/holdings")
@CrossOrigin(origins = "*")
public class StockHoldingController {

    private final StockHoldingRepository holdingRepo;
    private final UserRepository userRepo;

    public StockHoldingController(StockHoldingRepository holdingRepo, UserRepository userRepo) {
        this.holdingRepo = holdingRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<StockHolding> getAllHoldings() {
        return holdingRepo.findAll();
    }

    @PostMapping
    public StockHolding addHolding(@RequestBody HoldingRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        StockHolding holding = new StockHolding();
        holding.setStockSymbol(request.getSymbol());
        holding.setQuantity(request.getQuantity());
        holding.setUser(user);
        return holdingRepo.save(holding);
    }

    @GetMapping("/user/{userId}")
    public List<StockHolding> getHoldingsByUser(@PathVariable UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return holdingRepo.findByUser(user);
    }

    public static class HoldingRequest {
        private UUID userId;
        private String symbol;
        private int quantity;

        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
