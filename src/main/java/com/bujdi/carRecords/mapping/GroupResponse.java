package com.bujdi.carRecords.mapping;

import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.Vehicle;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GroupResponse {
    private UUID id;
    private String name;
    private Boolean readonly;
    private UUID owner_id;
    private String owner_name;

    public GroupResponse(Group group)
    {
        User owner = group.getOwner();
        this.id = group.getId();
        this.name = group.getName();
        this.readonly = group.isReadonly();
        this.owner_id = owner.getId();
        this.owner_name = owner.getDisplayName();
    }
}
