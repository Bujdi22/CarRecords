package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.GroupDto;
import com.bujdi.carRecords.mapping.GroupResponse;
import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.service.GroupService;
import com.bujdi.carRecords.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @GetMapping("/groups")
    public List<GroupResponse> getGroups() {
        User user = userService.getAuthUser();
        List<Group> groups = groupService.getGroupsOfUser(user);

        return groups.stream()
                .map(GroupResponse::new)
                .toList();
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupResponse> addGroup(@Valid @RequestBody GroupDto groupDto)
    {
        User user = userService.getAuthUser();
        Group group = groupService.addGroup(groupDto, user);

        return new ResponseEntity<>(new GroupResponse(group), HttpStatus.CREATED);
    }

    @PutMapping("/groups/{groupId}")
    public ResponseEntity<Object> updateGroup(
            @PathVariable("groupId") UUID groupId,
            @Valid @RequestBody GroupDto groupDto
    ){
        User user = userService.getAuthUser();

        Optional<Group> group = groupService.getGroupById(groupId);

        if (group.isEmpty() || group.get().getOwner().getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.groupService.updateGroup(group.get(), groupDto), HttpStatus.OK);
    }

    // todo group invites!
}
