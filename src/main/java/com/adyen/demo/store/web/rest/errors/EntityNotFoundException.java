package com.adyen.demo.store.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EntityNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entityName) {
        super(ErrorConstants.ENTITY_NOT_FOUND, entityName + " not found", Status.NOT_FOUND);
    }
}
