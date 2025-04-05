package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.GroupDto;
import com.bujdi.carRecords.dto.GroupInviteDto;
import com.bujdi.carRecords.dto.GroupInviteResponseDto;
import com.bujdi.carRecords.mapping.GroupResponse;
import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.SecureToken;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.service.GroupService;
import com.bujdi.carRecords.service.SecureTokenService;
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

    @Autowired
    SecureTokenService tokenService;

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

    @PostMapping("/groups/invite")
    public ResponseEntity<Object> sendInvite(
            @Valid @RequestBody GroupInviteDto groupInviteDto
    ) {
        User userToInvite = userService.findByUsername(groupInviteDto.getUsername());
        User authUser = userService.getAuthUser();

        if (userToInvite == null) {
            return new ResponseEntity<>("Could not find user with this e-mail address.",HttpStatus.FORBIDDEN);
        }

        List<Group> currentGroups = groupService.getGroupsOfUser(userToInvite);

        UUID groupId = groupInviteDto.getGroupId();

        boolean isMember = currentGroups.stream()
                .anyMatch(group -> group.getId().toString().equals(groupId.toString()));

        if (isMember) {
            return new ResponseEntity<>("This user is already attached to the group.",HttpStatus.FORBIDDEN);
        }

        Group group = groupService.getGroupById(groupId).orElseThrow();

        if (!group.getOwner().getId().equals(authUser.getId())) {
            return new ResponseEntity<>("You cannot make changes to this group", HttpStatus.FORBIDDEN);
        }

        if (groupService.hasPendingInvite(userToInvite, group)) {
            return new ResponseEntity<>("The user already has a pending invite.", HttpStatus.FORBIDDEN);
        }

        groupService.sendInvite(group, userToInvite);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/groups/invite-accept")
    public ResponseEntity<Object> acceptInvite(@Valid @RequestBody GroupInviteResponseDto dto) {
        SecureToken secureToken = tokenService.getTokenById(dto.getToken());

        if (secureToken == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.FORBIDDEN);
        }
        if (!secureToken.getPurpose().startsWith("group-invite:")) {
            return new ResponseEntity<>("Invalid token", HttpStatus.FORBIDDEN);
        }

        User authUser = userService.getAuthUser();
        if (!secureToken.getUser().getId().equals(authUser.getId())) {
            return new ResponseEntity<>("Invalid token", HttpStatus.FORBIDDEN);
        }

        groupService.acceptInvite(secureToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
