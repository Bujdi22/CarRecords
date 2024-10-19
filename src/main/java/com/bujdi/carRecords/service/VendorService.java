package com.bujdi.carRecords.service;

import com.bujdi.carRecords.model.Vendor;
import com.bujdi.carRecords.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    VendorRepository repo;

    public List<Vendor> getVendors() {
        return repo.findAll();
    }

    public boolean exists(int id) {
        return repo.existsById(id);
    }

    public Vendor getVendorById(int id) {
        return repo.findById(id).orElse(new Vendor());
    }
}
