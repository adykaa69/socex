package com.socex.social_extractor.domain.exception;

public class CompanyAlreadyExistsException extends ResourceAlreadyExistsException {
    public CompanyAlreadyExistsException(String name) {
        super("Company", name);
    }
}
