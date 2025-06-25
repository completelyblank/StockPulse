package com.stockpulse.stockpulse.dto;

import java.util.List;

public class PortfolioResponse {
    private List<HoldingResponse> holdings;
    private double totalValue;

    // Getters and Setters
    public List<HoldingResponse> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<HoldingResponse> holdings) {
        this.holdings = holdings;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
