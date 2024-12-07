package com.bujdi.carRecords.model;

import com.bujdi.carRecords.utils.AuditableField;
import com.bujdi.carRecords.validation.AccessValidatable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Audited(targetAuditMode = NOT_AUDITED)
public class Vehicle  implements AccessValidatable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    @AuditableField
    private String displayName;

    @Column(nullable = false)
    @AuditableField
    private String make;

    @Column(nullable = false)
    @AuditableField
    private String model;

    @Column(nullable = false)
    @AuditableField
    private String registration;

    @Column(nullable = false)
    @AuditableField
    private int year;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Override
    public boolean hasUserAccess(UUID userId) {
        User user = this.getUser();
        if (user == null) {
            return false;
        }

        return user.getId().equals(userId);
    }
}
