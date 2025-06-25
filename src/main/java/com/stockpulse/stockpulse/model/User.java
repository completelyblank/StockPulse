package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "`user`") // Escapes the reserved keyword
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 🔐 New field

    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockHolding> holdings = new ArrayList<>();

    // 🕒 Salary System Fields
    private double salaryAmount;
    private int salaryIntervalSeconds;

    // Getters and Setters
    public UUID getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public List<StockHolding> getHoldings() { return holdings; }

    public void setHoldings(List<StockHolding> holdings) { this.holdings = holdings; }

    public double getSalaryAmount() { return salaryAmount; }

    public void setSalaryAmount(double salaryAmount) { this.salaryAmount = salaryAmount; }

    public int getSalaryIntervalSeconds() { return salaryIntervalSeconds; }

    public void setSalaryIntervalSeconds(int salaryIntervalSeconds) { this.salaryIntervalSeconds = salaryIntervalSeconds; }
}
