package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Integer> {
}
