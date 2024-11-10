package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.MaintenanceRecordDto;
import com.bujdi.carRecords.dto.MaintenanceRecordUpdateDto;
import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.service.VehicleService;
import com.bujdi.carRecords.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class MaintenanceRecordController {

    @Autowired
    UserService userService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    MaintenanceRecordService recordService;


    @GetMapping("/maintenance-records/{vehicleId}")
    public ResponseEntity<Object> getRecords(@PathVariable("vehicleId") int vehicleId) {
        User user = userService.getAuthUser();

        Optional<Vehicle> vehicle = vehicleService.getVehicleById(vehicleId);

        if (vehicle.isEmpty() || vehicle.get().getUser().getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<MaintenanceRecord> records = recordService.getRecordsForVehicle(vehicleId);

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/maintenance-records/single-record/{recordId}")
    public ResponseEntity<Object> getSingleRecord(@PathVariable("recordId") int recordId) {
        User user = userService.getAuthUser();

        Optional<MaintenanceRecord> record = recordService.getRecordById(recordId);

        if (record.isEmpty() || record.get().getVehicle().getUser().getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(record.get(), HttpStatus.OK);
    }

    @PostMapping("/maintenance-records")
    public ResponseEntity<MaintenanceRecord> addRecord(@Valid @RequestBody MaintenanceRecordDto dto) {
        return new ResponseEntity<>(recordService.addRecord(dto), HttpStatus.OK);
    }

    @PutMapping("/maintenance-records/{recordId}")
    public ResponseEntity<Object> updateRecord(
        @PathVariable("recordId") int recordId,
        @Valid @RequestBody MaintenanceRecordUpdateDto dto
    ){
        User user = userService.getAuthUser();

        Optional<MaintenanceRecord> record = recordService.getRecordById(recordId);

        if (record.isEmpty() || record.get().getVehicle().getUser().getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.recordService.updateRecord(record.get(), dto), HttpStatus.OK);
    }

    @DeleteMapping("/maintenance-records/{recordId}")
    public ResponseEntity<Object> deleteRecord(@PathVariable("recordId") int recordId) {
        User user = userService.getAuthUser();

        Optional<MaintenanceRecord> record = recordService.getRecordById(recordId);

        if (record.isEmpty() || record.get().getVehicle().getUser().getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recordService.deleteRecord(record.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
