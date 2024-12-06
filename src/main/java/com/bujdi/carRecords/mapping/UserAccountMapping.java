package com.bujdi.carRecords.mapping;

import com.bujdi.carRecords.model.User;

import java.util.Map;

public class UserAccountMapping {
    public static Map<String, Object> mapUserToResponse(User user) {
        return Map.of(
                "id", user.getId(),
                "displayName", user.getDisplayName(),
                "username", user.getUsername(),
                "role", user.getRole(),
                "createdAt", user.getCreatedAt(),
                "verifiesWithPassword", user.getPassword() != null,
                "verifiesWithGoogle", user.getGoogleId() != null
        );
    }
}
