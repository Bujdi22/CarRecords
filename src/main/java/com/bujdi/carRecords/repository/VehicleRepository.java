package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByUserIdAndDeletedAtIsNull(int userId);
}
