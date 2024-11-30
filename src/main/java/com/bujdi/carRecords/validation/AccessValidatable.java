package com.bujdi.carRecords.validation;

import java.util.UUID;

public interface AccessValidatable {
    boolean hasUserAccess(UUID userId);
}
