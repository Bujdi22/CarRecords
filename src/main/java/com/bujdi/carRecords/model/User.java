package com.bujdi.carRecords.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
    private boolean emailVerified;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @ManyToMany(mappedBy = "members")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Group> ownedGroups = new HashSet<>();
}
