package com.stockpulse.stockpulse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceService {

    @Value("${polygon.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public Double getCurrentPrice(String symbol) {
        try {
            String url = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/prev?adjusted=true&apiKey=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                return results.get(0).path("c").asDouble();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch price for " + symbol + ": " + e.getMessage());
        }
        return 0.0;
    }

    public Object getAllPrices() {
        return null;
    }
}
