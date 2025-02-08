package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.VehicleDto;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.repository.VehicleRepository;
import com.bujdi.carRecords.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository repo;

    @Autowired
    PdfService pdfService;

    @Autowired
    MaintenanceRecordService maintenanceRecordService;

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

    public InputStreamResource toPdf(Vehicle vehicle) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", vehicle.getDisplayName());
        variables.put("make", vehicle.getMake());
        variables.put("model", vehicle.getModel());
        variables.put("year", vehicle.getYear());
        variables.put("created", DateTimeUtils.formatLocalDateTime(vehicle.getCreatedAt()));

        List<Map<String, Object>> records = maintenanceRecordService.getRecordsForVehicle(vehicle.getId())
            .stream()
            .map(record -> {
                Map<String, Object> map = new HashMap<>();
                map.put("title", record.getTitle());
                map.put("odometer", record.getOdometer().toString());
                map.put("createdAt", DateTimeUtils.formatLocalDateTime(record.getCreatedAt()));
                map.put("carriedOut", DateTimeUtils.formatLocalDate(record.getDate()));
                map.put("items", record.getDescriptionAsString());
                return map;
            })
            .toList();

        variables.put("records", records);

        byte[] pdfBytes = pdfService.generatePdf("vehicleTemplate.html", variables);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);

        return new InputStreamResource(inputStream);
    }
}
