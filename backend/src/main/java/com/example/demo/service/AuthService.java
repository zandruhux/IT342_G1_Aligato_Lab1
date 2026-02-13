package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new user with validation
     */
    public UserDTO registerUser(RegisterRequest request) {
        // Validate email is not already in use
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("user");
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Authenticate user and generate JWT token
     */
    public String authenticateUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        // Generate JWT token
        return jwtUtil.generateToken(user.getEmail());
    }

    /**
     * Get user by JWT token
     */
    public UserDTO getUserByToken(String token) {
        try {
            // Extract email from JWT
            String email = jwtUtil.extractUsername(token);
            
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            
            return convertToDTO(userOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }

    /**
     * Logout user (JWT tokens are stateless, so client-side removal is sufficient)
     */
    public void logoutUser(String token) {
        // With JWT, logout is handled client-side by removing the token
        // No server-side action needed unless implementing token blacklist
    }

    /**
     * Convert User entity to UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getUserId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole(),
            user.getIsActive(),
            user.getCreatedAt()
        );
    }
}
