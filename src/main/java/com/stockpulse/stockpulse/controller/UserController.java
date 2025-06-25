package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.User;
import com.stockpulse.stockpulse.dto.UserDTO;
import com.stockpulse.stockpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // No encryption
        user.setBalance(1000.0);     // Initial balance

        userRepo.save(user);
        return ResponseEntity.ok(user);
    }

    //  Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        // ✅ Use DTO instead of raw User
        UserDTO userDTO = new UserDTO(user, null);
        return ResponseEntity.ok(userDTO);
    }


    // Get All Users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    
 // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        UserDTO dto = new UserDTO(userOpt.get(), null);
        return ResponseEntity.ok(dto);
    }


    // Create User (manual/admin, if needed)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }
}
