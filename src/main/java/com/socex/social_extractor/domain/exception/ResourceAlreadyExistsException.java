package com.socex.social_extractor.domain.exception;

public abstract class ResourceAlreadyExistsException extends DomainException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    protected ResourceAlreadyExistsException(String fieldName , String resourceName) {
        super(String.format("%s already exists with name: %s", fieldName, resourceName));
    }
}
