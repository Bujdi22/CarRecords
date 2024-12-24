package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarModelController {

    @Autowired
    CarModelRepository carModelRepository;

    @GetMapping("/api/car-makes")
    public ResponseEntity<List<String>> getAllDistinctMakes() {
        List<String> distinctMakes = carModelRepository.findAllDistinctMakes();

        return ResponseEntity.ok(distinctMakes);
    }

    @GetMapping("/api/car-models")
    public ResponseEntity<Object> getMakes(
            @RequestParam(name = "make", required = false) String make
    ) {
        if (make == null || make.isEmpty()) {
            return new ResponseEntity<>("Make is required", HttpStatus.BAD_REQUEST);
        }

        List<String> models = carModelRepository.findByMakeIgnoreCase(make);
        return ResponseEntity.ok(models);
    }
}
