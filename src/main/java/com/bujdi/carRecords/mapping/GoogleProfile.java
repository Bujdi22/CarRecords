package com.bujdi.carRecords.mapping;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoogleProfile {
    private String id;
    private String email;
    private boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
}
