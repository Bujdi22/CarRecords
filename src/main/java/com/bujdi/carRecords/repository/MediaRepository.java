package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
}
