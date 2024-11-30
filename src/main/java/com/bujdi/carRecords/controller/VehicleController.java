package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.VehicleDto;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        User user = userService.getAuthUser();

        return service.getVehicles(user);
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
