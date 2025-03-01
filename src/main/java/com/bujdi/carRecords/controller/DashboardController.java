package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.mapping.VehicleResponse;
import com.bujdi.carRecords.mapping.MaintenanceRecordResponse;
import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.service.MaintenanceRecordService;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class DashboardController {
    @Autowired
    UserService userService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    MaintenanceRecordService recordService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> index() {
        User user = userService.getAuthUser();

        Optional<Vehicle> latestVehicle = vehicleService.getLatestVehicle(user);
        Optional<MaintenanceRecord> latestRecord = recordService.getLatestRecord(user);

        Map<String, Object> map = new HashMap<>();
        map.put("vehicleCount", vehicleService.getVehicleCount(user));
        map.put("recordCount", recordService.getMaintenanceRecordCount(user));
        map.put("vehicle", latestVehicle.map(vehicle -> vehicleService.toVehicleResponse(vehicle)).orElse(null));
        map.put("record", latestRecord.map(MaintenanceRecordResponse::new).orElse(null));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
