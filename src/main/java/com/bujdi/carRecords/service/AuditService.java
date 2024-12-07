package com.bujdi.carRecords.service;

import com.bujdi.carRecords.utils.AuditableField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class AuditService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> fetchAuditData(Class<?> auditableClass, UUID auditableId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Number> revisions = auditReader.getRevisions(auditableClass, auditableId);

        List <Map<String, Object>> auditLogs = new ArrayList<>();

        List<String> auditableFields = getAuditableFieldNames(auditableClass);

        for (int i = 1; i < revisions.size(); i++) {
            Number currentRev = revisions.get(i);
            Number previousRev = revisions.get(i - 1);

            Object currentEntity = auditReader.find(auditableClass, auditableId, currentRev);
            Object previousEntity = auditReader.find(auditableClass, auditableId, previousRev);

            Date revisionDate = auditReader.getRevisionDate(currentRev);

            for (String fieldName : auditableFields) {
                try {
                    Field field = auditableClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object currentValue = field.get(currentEntity);
                    Object previousValue = field.get(previousEntity);

                    if (!Objects.equals(currentValue, previousValue)) {
                        auditLogs.add(createAuditEntry(revisionDate, fieldName, previousValue, currentValue));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return auditLogs;
    }

    public List<String> getAuditableFieldNames(Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AuditableField.class)) {
                fields.add(field.getName());
            }
        }

        return fields;
    }

    private Map<String, Object> createAuditEntry(Date revisionDate, String fieldName, Object previousValue, Object currentValue) {
        Map<String, Object> auditEntry = new HashMap<>();
        auditEntry.put("event_happened_on", revisionDate);
        auditEntry.put("updated_field", fieldName);
        auditEntry.put("old_value", previousValue != null ? previousValue.toString() : null);
        auditEntry.put("new_value", currentValue != null ? currentValue.toString() : null);

        return auditEntry;
    }
}
