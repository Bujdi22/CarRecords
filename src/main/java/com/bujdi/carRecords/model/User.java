package com.bujdi.carRecords.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String displayName;
    private String password;
    private String role = "ROLE_USER";
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
