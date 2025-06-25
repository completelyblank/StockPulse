package com.stockpulse.stockpulse.dto;

import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.service.StockPriceService;

public class HoldingResponse {
    private String symbol;
    private int quantity;
    private double avgBuyPrice;
    private double currentPrice;
    private double gainLoss;

    public HoldingResponse() {}
    public HoldingResponse(StockHolding holding, StockPriceService priceService) {
        this.symbol = holding.getStockSymbol();
        this.quantity = holding.getQuantity();
        this.avgBuyPrice = holding.getAvgBuyPrice();
        this.currentPrice = priceService.getCurrentPrice(this.symbol);
        this.gainLoss = this.currentPrice * this.quantity - this.avgBuyPrice * this.quantity;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = gainLoss;
    }
}
