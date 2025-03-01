package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, UUID> {
    List<MaintenanceRecord> findByVehicleIdAndDeletedAtIsNull(UUID vehicleId);
    Optional<MaintenanceRecord> findByIdAndDeletedAtIsNull(UUID maintenanceRecordId);
    int countByVehicleIdAndDeletedAtIsNull(UUID vehicleId);

    @Query("SELECT COUNT(m) FROM MaintenanceRecord m JOIN Vehicle v ON m.vehicle.id = v.id WHERE v.user.id = :userId AND m.deletedAt IS NULL")
    long countByUserIdAndDeletedAtIsNull(UUID userId);

    @Query("SELECT mr FROM MaintenanceRecord mr " +
            "JOIN Vehicle v ON mr.vehicle.id = v.id " +
            "WHERE v.user.id = :userId AND v.deletedAt IS NULL AND mr.deletedAt IS NULL " +
            "ORDER BY mr.createdAt DESC LIMIT 1")
    Optional<MaintenanceRecord> findLatestByUserIdAndDeletedAtIsNull(UUID userId);
}
