package com.stockpulse.stockpulse.dto;

import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.service.StockPriceService; // Add this
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDTO {
    private UUID id;
    private String username;
    private double balance;
    private List<HoldingResponse> holdings;
    private double salaryAmount;
    private int salaryIntervalSeconds;

    // Accept StockPriceService as parameter
    public UserDTO(User user, StockPriceService stockPriceService) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.balance = user.getBalance();
        this.salaryAmount = user.getSalaryAmount();
        this.salaryIntervalSeconds = user.getSalaryIntervalSeconds();
        this.holdings = user.getHoldings().stream()
            .map(holding -> new HoldingResponse())
            .collect(Collectors.toList());
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public List<HoldingResponse> getHoldings() { return holdings; }
    public double getSalaryAmount() { return salaryAmount; }
    public int getSalaryIntervalSeconds() { return salaryIntervalSeconds; }
}
