package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Integer> {
    @Query("SELECT t FROM SecureToken t WHERE t.token = :token")
    SecureToken findToken(String token);
}
