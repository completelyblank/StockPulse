package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.dto.SalaryRequest;
import com.stockpulse.stockpulse.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @PostMapping("/configure")
    public ResponseEntity<?> setSalary(@RequestBody SalaryRequest request) {
        salaryService.configureSalary(request.getUserId(), request.getAmount(), request.getIntervalSeconds());
        return ResponseEntity.ok("Salary configured.");
    }

}
