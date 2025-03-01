package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findByUserIdAndDeletedAtIsNull(UUID userId);

    Optional<Vehicle> findByIdAndDeletedAtIsNull(UUID vehicleId);

    // Custom method to get the count of vehicles for a specific user where deletedAt is null
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.user.id = :userId AND v.deletedAt IS NULL")
    long countByUserIdAndDeletedAtIsNull(UUID userId);

    @Query("SELECT v FROM Vehicle v WHERE v.user.id = :userId AND v.deletedAt IS NULL ORDER BY v.createdAt DESC LIMIT 1")
    Optional<Vehicle> findLatestByUserIdAndDeletedAtIsNull(UUID userId);
}
