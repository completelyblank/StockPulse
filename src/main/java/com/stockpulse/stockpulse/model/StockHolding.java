package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class StockHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String stockSymbol;
    private Double avgBuyPrice = 0.0;
    private Double currentPrice = 0.0;
    private Integer quantity = 0;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor (required by JPA)
    public StockHolding() {}

    // Custom constructor for convenience
    public StockHolding(UUID id, String stockSymbol, int quantity, User user) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.user = user;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAvgBuyPrice() {
    	return (avgBuyPrice != null) ? avgBuyPrice : 0.0;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

	}

	





