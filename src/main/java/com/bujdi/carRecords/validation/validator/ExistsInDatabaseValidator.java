package com.bujdi.carRecords.validation.validator;

import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

@Slf4j
public class ExistsInDatabaseValidator implements ConstraintValidator<ExistsInDatabase, Object> {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    private Class<?> entity;
    private String field;
    private boolean belongsToUser;

    @Override
    public void initialize(ExistsInDatabase constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
        this.belongsToUser = constraintAnnotation.belongsToUser();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<?> root = query.from(entity);

            Predicate mainPredicate = cb.equal(root.get(field), value);

            if (belongsToUser) {
                int userId = userService.getAuthUser().getId();
                Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
                mainPredicate = cb.and(mainPredicate, userPredicate);
            }

            query.select(cb.count(root)).where(mainPredicate);

            Long count = entityManager.createQuery(query).getSingleResult();

            return count != null && count > 0;
        } catch (Throwable t) {
            log.error(String.valueOf(t));
        }
        return false;
    }
}