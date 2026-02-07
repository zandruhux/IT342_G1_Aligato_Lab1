package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.AuthToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;

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
     * Authenticate user and generate token
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

        // Generate token
        String tokenValue = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setToken(tokenValue);
        authToken.setExpiration(LocalDateTime.now().plusHours(24));

        tokenRepository.save(authToken);
        return tokenValue;
    }

    /**
     * Get user by token
     */
    public UserDTO getUserByToken(String token) {
        Optional<AuthToken> authTokenOpt = tokenRepository.findByToken(token);

        if (authTokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }

        AuthToken authToken = authTokenOpt.get();

        // Check if token is expired
        if (authToken.isExpired()) {
            tokenRepository.delete(authToken);
            throw new RuntimeException("Token has expired");
        }

        return convertToDTO(authToken.getUser());
    }

    /**
     * Logout user by removing token
     */
    public void logoutUser(String token) {
        Optional<AuthToken> authTokenOpt = tokenRepository.findByToken(token);
        authTokenOpt.ifPresent(tokenRepository::delete);
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
