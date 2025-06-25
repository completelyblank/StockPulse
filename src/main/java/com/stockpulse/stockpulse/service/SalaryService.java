package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalaryService {

    @Autowired
    private UserRepository userRepo;

    // Runs every 10 seconds (example); for configurable intervals, use DB flags
    @Scheduled(fixedRate = 10000)
    public void creditSalaries() {
        List<User> users = userRepo.findAll();

        for (User user : users) {
            if (user.getSalaryAmount() > 0) {
                user.setBalance(user.getBalance() + user.getSalaryAmount());
                System.out.println("Credited salary to: " + user.getUsername());
            }
        }

        userRepo.saveAll(users);
    }

    public void configureSalary(UUID userId, double amount, int intervalSeconds) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setSalaryAmount(amount);
        user.setSalaryIntervalSeconds(intervalSeconds);
        userRepo.save(user);
    }
}
