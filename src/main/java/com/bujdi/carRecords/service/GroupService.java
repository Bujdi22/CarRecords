package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.GroupDto;
import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    public Optional<Group> getGroupById(UUID groupId) {
        return groupRepository.findById(groupId);
    }

    public List<Group> getGroupsOfUser(User user) {
        return groupRepository.findAllByMember(user);
    }

    public Group addGroup(GroupDto dto, User user) {
        Group group = new Group();

        group.setReadonly(dto.getReadonly());
        group.setName(dto.getName());
        LocalDateTime now = LocalDateTime.now();
        group.setCreatedAt(now);
        group.setOwner(user);

        groupRepository.save(group);

        addUserToGroup(group, user);

        return group;
    }

    public Group updateGroup(Group group, GroupDto dto) {
        group.setName(dto.getName());
        group.setReadonly(dto.getReadonly());

        return groupRepository.save(group);
    }

    public void addUserToGroup(Group group, User user) {
        group.getMembers().add(user);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(Group group, User user) {
        group.getMembers().remove(user);
        groupRepository.save(group);
    }

    // todo delete
}
