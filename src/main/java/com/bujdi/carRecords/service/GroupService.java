package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.GroupDto;
import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.SecureToken;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.repository.GroupRepository;
import com.bujdi.carRecords.repository.SecureTokenRepository;
import com.bujdi.carRecords.utils.UrlGenerator;
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

    @Autowired
    private SecureTokenRepository tokenRepo;

    @Autowired
    private EmailService emailService;

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

    public void sendInvite(Group group, User user) {
        SecureToken token = new SecureToken();
        token.setUser(user);
        token.setPurpose(getSecureTokenKey(group));
        token.setExpiryDate(LocalDateTime.now().plusDays(14));
        tokenRepo.save(token);

        String acceptLink = UrlGenerator.generateUrl("/group-invite?token=" + token.getToken());
        String content = "<p>You received an invite to join a group named: <i>" + group.getName() +"</i></p>"
                + "<p>You will be able to view vehicle records in this group.</p>"
                + "<p>The invite is from: <i>" + group.getOwner().getDisplayName() +"</i></p>"
                + "<p>Please follow the link below to accept or decline the invite.</p>"
                + "<p><a href='"+ acceptLink +"'>View invite</a></p>";
        String subject = "AutoJournal: New group invite";

        emailService.sendEmail(user, subject, content);
    }

    public String getSecureTokenKey(Group group) {
        return "group-invite:" + group.getId();
    }

    public boolean hasPendingInvite(User user, Group group) {
        return tokenRepo.existsByUserIdAndPurposeAndExpiryDateAfter(user.getId(), getSecureTokenKey(group), LocalDateTime.now());
    }

    public void acceptInvite(SecureToken token) {
        if (!token.getPurpose().startsWith("group-invite:")) {
            throw new IllegalArgumentException("Invalid token received in Group Service acceptInvite.");
        }

        String uuidString = token.getPurpose().split(":")[1];
        Group group = getGroupById(UUID.fromString(uuidString)).orElseThrow();

        User user = token.getUser();

        addUserToGroup(group, user);

        tokenRepo.delete(token);
    }
}
