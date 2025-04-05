package com.bujdi.carRecords.service;

import com.bujdi.carRecords.model.SecureToken;
import com.bujdi.carRecords.repository.SecureTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecureTokenService {

    @Autowired
    private SecureTokenRepository tokenRepo;

    // todo refactor logic from other services to use this new service

    public SecureToken getTokenById(String id)
    {
        SecureToken secureToken = tokenRepo.findToken(id);
        if (secureToken != null && secureToken.isExpired()) {
            return null;
        }
        return secureToken;
    }
}
