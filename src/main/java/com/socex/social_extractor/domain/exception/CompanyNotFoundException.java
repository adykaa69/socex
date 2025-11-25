package com.socex.social_extractor.domain.exception;

import java.util.UUID;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(UUID id) {
        super("Company", id);
    }
}
