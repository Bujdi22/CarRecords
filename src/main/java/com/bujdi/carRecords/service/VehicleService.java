package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.VehicleDto;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository repo;

    public List<Vehicle> getVehicles(User user) {
        return repo.findByUserIdAndDeletedAtIsNull(user.getId());
    }

    public Vehicle addVehicle(Vehicle vehicle, User user) {
        LocalDateTime now = LocalDateTime.now();
        vehicle.setCreatedAt(now);
        vehicle.setUpdatedAt(now);
        vehicle.setUser(user);

        return repo.save(vehicle);
    }

    public Vehicle updateVehicle(Vehicle vehicle, VehicleDto dto) {
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setDisplayName(dto.getDisplayName());
        vehicle.setYear(dto.getYear());
        vehicle.setRegistration(dto.getRegistration());
        vehicle.setUpdatedAt(LocalDateTime.now());

        return repo.save(vehicle);
    }

    public Optional<Vehicle> getVehicleById(UUID vehicleId) {
        return repo.findByIdAndDeletedAtIsNull(vehicleId);
    }

    public void deleteVehicle(Vehicle vehicle) {
        vehicle.setDeletedAt(LocalDateTime.now());
        repo.save(vehicle);
    }
}
