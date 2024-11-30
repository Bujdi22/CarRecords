package com.bujdi.carRecords.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String displayName;
    private String password;
    private String role = "ROLE_USER";
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String googleId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
