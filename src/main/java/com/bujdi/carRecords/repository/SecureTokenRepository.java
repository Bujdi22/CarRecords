package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Integer> {
    @Query("SELECT t FROM SecureToken t WHERE t.token = :token")
    SecureToken findToken(String token);

    boolean existsByUserIdAndPurposeAndExpiryDateAfter(UUID userId, String purpose, LocalDateTime now);

}
