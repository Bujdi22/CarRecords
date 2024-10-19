package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.model.Vendor;
import com.bujdi.carRecords.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VendorController {

    @Autowired
    VendorService service;

    @GetMapping("/api/vendors")
    public List<Vendor> getVendors() {
        return service.getVendors();
    }
}
