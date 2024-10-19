package com.bujdi.carRecords.validation.validator;

import com.bujdi.carRecords.validation.annotation.UniqueInDatabase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UniqueConstraintValidator implements ConstraintValidator<UniqueInDatabase, Object> {

    @Autowired
    private EntityManager entityManager;
    private Class<?> entity;
    private String field;

    @Override
    public void initialize(UniqueInDatabase constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<?> root = query.from(entity);
        query.select(cb.count(root)).where(cb.equal(root.get(field), value));

        Long count = entityManager.createQuery(query).getSingleResult();
        return count == 0;
    }
}