package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
    @Query("SELECT m FROM Media m WHERE m.modelId = :modelId AND m.modelType = :modelType")
    List<Media> findMediaByModelIdAndModelType(@Param("modelId") UUID modelId, @Param("modelType") String modelType);
}
