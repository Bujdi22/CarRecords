package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Integer> {
    @Query("SELECT c.model FROM CarModel c WHERE LOWER(c.make) = LOWER(:make) ORDER BY c.model ASC")
    List<String> findByMakeIgnoreCase(@Param("make") String make);

    @Query("SELECT DISTINCT c.make FROM CarModel c ORDER BY c.make ASC")
    List<String> findAllDistinctMakes();

}
