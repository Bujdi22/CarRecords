package com.bujdi.carRecords.repository;

import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findByOwner(User owner);
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m = :user")
    List<Group> findAllByMember(@Param("user") User user);
}
