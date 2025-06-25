package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.Stock;
import com.stockpulse.stockpulse.repository.StockRepository;
import com.stockpulse.stockpulse.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    // Get all stock records from DB (if using Stock entity)
    @GetMapping
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Add a new stock record (initial DB entry)
    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        return stockRepository.save(stock);
    }

    // Get current price of a stock
    @GetMapping("/price")
    public ResponseEntity<?> getPrice(@RequestParam String symbol) {
        double price = stockPriceService.getCurrentPrice(symbol);
        return ResponseEntity.ok(Map.of("symbol", symbol.toUpperCase(), "price", price));
    }

    // Get all current stock prices (in-memory map)
    @GetMapping("/prices")
    public ResponseEntity<Object> getAllPrices() {
        return ResponseEntity.ok(stockPriceService.getAllPrices());
    }
}
