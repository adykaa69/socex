package com.socex.social_extractor.adapters.inbound.web.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    String errorCode,
    Instant timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ValidationError> validationErrors
) {
    public ErrorResponse(String errorCode) {
        this(errorCode, Instant.now(), null);
    }

    public ErrorResponse(String errorCode, List<ValidationError> validationErrors) {
        this(errorCode, Instant.now(), validationErrors);
    }
}
