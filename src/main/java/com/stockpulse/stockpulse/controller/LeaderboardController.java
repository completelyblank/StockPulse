package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.dto.LeaderboardEntry;
import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.repository.UserRepository;
import com.stockpulse.stockpulse.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StockPriceService stockPriceService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard() {
        List<User> users = userRepo.findAll();
        List<LeaderboardEntry> entries = new ArrayList<>();

        for (User user : users) {
            double holdingsValue = 0.0;

            for (StockHolding holding : user.getHoldings()) {
                try {
                    double currentPrice = stockPriceService.getCurrentPrice(holding.getStockSymbol());
                    holdingsValue += holding.getQuantity() * currentPrice;
                } catch (Exception e) {
                    System.err.println("Failed to fetch price for: " + holding.getStockSymbol());
                }
            }

            double totalValue = user.getBalance() + holdingsValue;
            double gainLoss = totalValue - user.getBalance(); // startingBalance must be present in User

            entries.add(new LeaderboardEntry(0, user.getUsername(), totalValue, gainLoss));
        }

        // Sort by totalValue in descending order
        entries.sort((a, b) -> Double.compare(b.getTotalValue(), a.getTotalValue()));

        // Assign ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }

        return ResponseEntity.ok(entries);
    }
}
