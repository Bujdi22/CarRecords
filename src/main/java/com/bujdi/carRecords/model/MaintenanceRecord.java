package com.bujdi.carRecords.model;

import com.bujdi.carRecords.validation.AccessValidatable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MaintenanceRecord implements AccessValidatable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Vehicle vehicle;

    @Column(nullable = false)
    private String title;

    @JsonIgnore
    @Column(name = "description_json", columnDefinition = "TEXT")
    private String descriptionJson;

    private LocalDate date;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    public void setDescription(Map<String, Object> description) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.descriptionJson = objectMapper.writeValueAsString(description);
            System.out.println("json=" + this.descriptionJson);
        } catch (JsonProcessingException ex) {
            System.out.println("Failed to serialize JSON: " + ex.getMessage());
        }
    }

    public Map<String, Object> getDescription() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(descriptionJson, Map.class);
        } catch (IOException ex) {
            System.out.println("Failed to deserialize JSON: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean hasUserAccess(UUID userId) {
        if (vehicle == null) {
            return false;
        }
        User user = vehicle.getUser();
        if (user == null) {
            return false;
        }

        return user.getId().equals(userId);
    }
}
