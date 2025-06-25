package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.dto.PortfolioResponse;
import com.stockpulse.stockpulse.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public PortfolioResponse getPortfolio(@PathVariable UUID userId) {
        return portfolioService.getPortfolio(userId);
    }
}
