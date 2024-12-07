package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.service.AuditService;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.utils.ModelTypeMapper;
import com.bujdi.carRecords.validation.AccessValidatable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class AuditController {

    @Autowired
    private AuditService auditService;
    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/api/audit")
    public ResponseEntity<Object> get(
            @RequestParam @NotNull(message = "modelType is required") String modelType,
            @RequestParam @NotNull(message = "modelId is required") UUID modelId
    ) {
        Class<?> modelClass = ModelTypeMapper.getModelClass(modelType);

        if (modelClass == null) {
            return new ResponseEntity<>("Wrong model type", HttpStatus.NOT_FOUND);
        }

        AccessValidatable auditableObject = (AccessValidatable) entityManager.find(modelClass, modelId);

        if (auditableObject == null || !auditableObject.hasUserAccess(userService.getAuthUser().getId())) {
            return new ResponseEntity<>("Model not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Map.of("items", auditService.fetchAuditData(modelClass, modelId)), HttpStatus.OK);
    }
}
