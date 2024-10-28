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

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody VehicleDto vehicleDto)
    {
        User user = userService.getAuthUser();
        Vehicle savedVehicle = service.addVehicle(this.createVehicleFromDTO(vehicleDto), user);

        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    private Vehicle createVehicleFromDTO(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();

        vehicle.setDisplayName(vehicleDto.getDisplayName());
        vehicle.setMake(vehicleDto.getMake());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setYear(vehicleDto.getYear());

        return vehicle;
    }
}
