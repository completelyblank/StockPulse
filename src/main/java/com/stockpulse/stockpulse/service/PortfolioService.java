package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.dto.*;
import com.stockpulse.stockpulse.model.StockHolding;
import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockPriceService stockPriceService;

    public PortfolioResponse getPortfolio(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<HoldingResponse> holdings = new ArrayList<>();
        double totalValue = 0.0;

        for (StockHolding holding : user.getHoldings()) {
        	Double avgBuyPrice = holding.getAvgBuyPrice() != null ? holding.getAvgBuyPrice() : 0.0;
        	Integer quantity = holding.getQuantity();
        	if (quantity == null) quantity = 0;
        	Double currentPrice = stockPriceService.getCurrentPrice(holding.getStockSymbol());
            double cost = avgBuyPrice * quantity;
            double holdingValue = currentPrice * quantity;

            HoldingResponse hr = new HoldingResponse();
            hr.setSymbol(holding.getStockSymbol());
            hr.setQuantity(quantity);
            hr.setAvgBuyPrice(avgBuyPrice);
            hr.setCurrentPrice(currentPrice);
            hr.setGainLoss(holdingValue - cost);

            holdings.add(hr);
            totalValue += holdingValue;
        }

        PortfolioResponse response = new PortfolioResponse();
        response.setHoldings(holdings);
        response.setTotalValue(totalValue);
        return response;
    }
}
