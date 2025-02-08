package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.VehicleDto;
import com.bujdi.carRecords.mapping.VehicleResponse;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.service.MaintenanceRecordService;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class VehicleController {

    @Autowired
    VehicleService service;

    @Autowired
    UserService userService;

    @Autowired
    MaintenanceRecordService recordService;

    @GetMapping("/vehicles")
    public List<VehicleResponse> getVehicles() {
        try {
            User user = userService.getAuthUser();

            return service.getVehicles(user)
                    .stream()
                    .map((vehicle -> {
                        int count = recordService.getRecordCountForVehicle(vehicle.getId());
                        return new VehicleResponse(vehicle, count);
                    }))
                    .toList();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @GetMapping("/vehicles/export/{vehicleId}")
    public ResponseEntity<Object> exportVehicle(@PathVariable("vehicleId") UUID vehicleId) {
        User user = userService.getAuthUser();

        Optional<Vehicle> optionalVehicle = service.getVehicleById(vehicleId);

        if (optionalVehicle.isEmpty() || !optionalVehicle.get().hasUserAccess(user.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vehicle vehicle = optionalVehicle.get();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + vehicle.getDisplayName() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(service.toPdf(vehicle));
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Object> getVehicle(@PathVariable("vehicleId") UUID vehicleId) {
        User user = userService.getAuthUser();

        Optional<Vehicle> vehicle = service.getVehicleById(vehicleId);

        if (vehicle.isEmpty() || !vehicle.get().hasUserAccess(user.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody VehicleDto vehicleDto)
    {
        User user = userService.getAuthUser();
        Vehicle savedVehicle = service.addVehicle(vehicleDto.toVehicle(), user);

        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @PutMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Object> updateVehicle(
        @PathVariable("vehicleId") UUID vehicleId,
        @Valid @RequestBody VehicleDto vehicleDto
    ){
        User user = userService.getAuthUser();

        Optional<Vehicle> vehicle = service.getVehicleById(vehicleId);

        if (vehicle.isEmpty() || !vehicle.get().hasUserAccess(user.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.service.updateVehicle(vehicle.get(), vehicleDto), HttpStatus.OK);
    }

    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable("vehicleId") UUID vehicleId) {

        User user = userService.getAuthUser();

        Optional<Vehicle> vehicle = service.getVehicleById(vehicleId);

        if (vehicle.isEmpty() || !vehicle.get().hasUserAccess(user.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        service.deleteVehicle(vehicle.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
