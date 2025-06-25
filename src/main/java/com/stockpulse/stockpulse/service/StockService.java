package com.stockpulse.stockpulse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class StockService {

    private final Map<String, Double> stockPrices = new ConcurrentHashMap<>();

    @Value("${polygon.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        stockPrices.put("AAPL", 180.0);
        stockPrices.put("GOOGL", 2700.0);
        stockPrices.put("MSFT", 330.0);
        stockPrices.put("TSLA", 800.0);
        stockPrices.put("AMZN", 125.0);
    }

    @Scheduled(fixedRate = 60000) // optional simulated updates
    public void updateStockPrices() {
        stockPrices.replaceAll((symbol, price) -> {
            double change = ThreadLocalRandom.current().nextDouble(-3.0, 3.0);
            return Math.max(1.0, price + change);
        });
    }

    public Map<String, Double> getStockPrices() {
        return stockPrices;
    }

    public double getPriceForSymbol(String symbol) {
        symbol = symbol.toUpperCase();

        // 1. Return simulated price if known
        if (stockPrices.containsKey(symbol)) {
            return stockPrices.get(symbol);
        }

        // 2. Else fetch real-time price from Polygon
        try {
            String url = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/prev?adjusted=true&apiKey=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                double price = results.get(0).path("c").asDouble();
                // Optionally cache
                stockPrices.put(symbol, price);
                return price;
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch real-time price for: " + symbol + " → " + e.getMessage());
        }

        throw new IllegalArgumentException("Unknown stock symbol: " + symbol);
    }
}
