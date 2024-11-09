package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Integer> {
    List<MaintenanceRecord> findByVehicleIdAndDeletedAtIsNull(int vehicleId);
    Optional<MaintenanceRecord> findByIdAndDeletedAtIsNull(int maintenanceRecordId);
}
