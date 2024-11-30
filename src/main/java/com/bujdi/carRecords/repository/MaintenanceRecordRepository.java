package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, UUID> {
    List<MaintenanceRecord> findByVehicleIdAndDeletedAtIsNull(UUID vehicleId);
    Optional<MaintenanceRecord> findByIdAndDeletedAtIsNull(UUID maintenanceRecordId);
}
