package com.socex.social_extractor.domain.exception;

import java.util.UUID;

public abstract class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    protected ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s not found with id: %s", resourceName, id));
    }
}
