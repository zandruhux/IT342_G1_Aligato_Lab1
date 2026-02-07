package com.example.demo.repository;

import com.example.demo.model.AuthToken;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByExpirationBefore(LocalDateTime dateTime);
}
